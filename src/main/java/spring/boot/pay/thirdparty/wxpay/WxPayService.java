package spring.boot.pay.thirdparty.wxpay;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import spring.boot.pay.common.MoneyFormat;
import spring.boot.pay.common.XmlHelper;
import spring.boot.pay.common.http.HttpClientHelper;
import spring.boot.pay.config.dictionary.PayType;
import spring.boot.pay.config.properties.WxPayProperties;
import spring.boot.pay.dao.TradeDao;
import spring.boot.pay.framework.base.PayTask;
import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.model.OpenIdInfo;
import spring.boot.pay.model.Trade;

import javax.net.ssl.SSLContext;

import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class WxPayService implements InitializingBean {

	public static final Logger LOGGER = LoggerFactory.getLogger(WxPayService.class);

	private HttpClientHelper HttpClient = HttpClientHelper.create();

	private CloseableHttpClient refundClient;

	private static final String PAY_TEMPLATE = "{\"appId\":\"%s\",\"timeStamp\":\"%s\",\"nonceStr\":\"%s\","
			+ "\"package\":\"%s\",\"signType\":\"MD5" + "\",\"paySign\":\"%s\"}";
	private static final String PAY_TEMPLATE_NOJSON = "appId=%s,timeStamp=%s,nonceStr=%s,"
			+ "package=%s,signType=MD5,paySign=%s}";
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	TradeDao tradeDao;

	/**
	 * 创建预支付请求的xml参数
	 */
	private String createXmlParams4PrePay(Trade trade) {

		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("appid", WxPayProperties.APP_ID);
		params.put("body", trade.getBody());
		params.put("mch_id", WxPayProperties.MCH_ID);
		params.put("nonce_str", WxPayHelper.getNonceStr());
		params.put("notify_url", WxPayProperties.DOMAIN + WxPayProperties.NOTIFY_URL);
		params.put("out_trade_no", trade.getOrderId());
		LOGGER.error("支付参数" + WxPayProperties.DOMAIN + WxPayProperties.NOTIFY_URL);
		if (PayType.WX_PAY_JSAPI == trade.getPayType()) {
			params.put("openid", trade.getOpenId());
		}

		params.put("spbill_create_ip", trade.getIp());
		params.put("total_fee", String.valueOf(MoneyFormat.yuan2fen(String.valueOf(trade.getTotalFee()))));

		if (PayType.WX_PAY_QC == trade.getPayType()) {
			params.put("trade_type", WxPayProperties.TRADE_TYPE_QC);
		} else {
			params.put("trade_type", WxPayProperties.TRADE_TYPE_JSAPI);
		}
        
		trade.setRequestTime(java.time.LocalDateTime.now());
		params.put("time_start", trade.getRequestTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		if(trade.getOrderType()!=-1){
		params.put("time_expire",
				trade.getRequestTime().plusSeconds(45).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
				// params.put("limit_pay","no_credit");//指定不能使用信用卡支付
		}
		
		return WxPayHelper.getRequestXml(params);

	}

	/**
	 * 发送https请求到微信获取 预支付id
	 */
	private Map<String, String> acquirePrePayReturnInfo(String xmlParam) {

		String str = HttpClient.execute(xmlParam, WxPayProperties.WX_PRE_PAY_URL, 1);

		Map<String, String> map = XmlHelper.parseXmlToMap(str);
		map.put("return", str);

		return map;
	}

	/**
	 * 根据预支付信息创建用于支付的json串
	 * 
	 * @param packageStr
	 *            预支付信息
	 * @return 用于提交给微信进行支付的json串
	 */
	private String cretePayJson(String packageStr) {

		SortedMap<String, String> map = new TreeMap<>();
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		map.put("appId", WxPayProperties.APP_ID);
		map.put("nonceStr", WxPayHelper.getNonceStr());
		map.put("timeStamp", timestamp);
		map.put("package", packageStr);
		map.put("signType", "MD5");

		return String.format(PAY_TEMPLATE, WxPayProperties.APP_ID, timestamp, map.get("nonceStr"), packageStr,
				WxPayHelper.createSign(map));
	}

	/**
	 * 根据支付信息 生成微信支付的请求串
	 */
	public ResultModel getPayJson(Trade trade) {

		
		
		String xmlParams = createXmlParams4PrePay(trade);
		trade.setRequestContent(xmlParams);
		trade.setTradeStatus(PayTask.STATUS_ACCEPTED);

		if (StringUtils.isBlank(trade.getOrderId())) {

			//错误处理
		}
		tradeDao.checkOrderStatus(trade);
		if (trade.getTradeStatus() != -1) {// 交易表中没订单，则插入订单
			tradeDao.save(trade);
		}
		
	
		Map<String, String> map = acquirePrePayReturnInfo(xmlParams);
		String prePayId = map.get("prepay_id");
		String codeUrl = map.get("code_url");

		trade.setReturnContent(map.get("return"));
		tradeDao.updateReturnContent(trade);
		LOGGER.error("获取预支付id失败,xmlParams is:{},resp is:{}", xmlParams, map.get("return"));
		if (prePayId == null) {
			LOGGER.error("获取预支付id失败,xmlParams is:{},resp is:{}", xmlParams, map.get("return"));

			tradeDao.updateStatus(PayTask.STATUS_FirstFailed_pre, trade.getOrderId());

			return ResultModel.failModel("获取预支付id失败");
		}

		if (trade.getPayType() == PayType.WX_PAY_QC) {

			if (codeUrl == null) {

				LOGGER.error("获取二维码连接失败,xmlParams is:{},resp is:{}", xmlParams, map.get("return"));

				tradeDao.updateStatus(PayTask.STATUS_FirstFailed_qc, trade.getOrderId());

				return ResultModel.failModel("获取二维码连接失败");
			} else {

				String wxPayParam = cretePayJson("prepay_id=" + prePayId + "$code_url=" + codeUrl);

				return ResultModel.successModel(wxPayParam);
			}
		}

		if (trade.getPayType() == PayType.WX_PAY_JSAPI) {
			return ResultModel.successModel(prePayId);
		}

		String wxPayParam = cretePayJson("prepay_id=" + prePayId);

		return ResultModel.successModel(wxPayParam);
	}

	

	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

}
