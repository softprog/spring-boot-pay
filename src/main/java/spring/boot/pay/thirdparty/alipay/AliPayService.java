package spring.boot.pay.thirdparty.alipay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.boot.pay.config.properties.AliPayProperties;
import spring.boot.pay.dao.TradeDao;
import spring.boot.pay.framework.base.PayTask;
import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.model.Trade;

@Service
public class AliPayService implements InitializingBean {

	
	@Autowired
	TradeDao tradeDao;

	public ResultModel sendDataForDirectPayForbank(Trade trade) {

		
		//////////////////////////////////// 请求参数//////////////////////////////////////

		// 支付类型
		String payment_type = "1";
		// 必填，不能修改
		// 服务器异步通知页面路径
		String notify_url = AliPayProperties.domain+AliPayProperties.notify_url;
		// 需http://格式的完整路径，不能加?id=123这类自定义参数

		// 页面跳转同步通知页面路径
		String return_url =AliPayProperties.return_url;
		// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

		// 商户订单号
		String out_trade_no = trade.getOrderId();
		// 商户网站订单系统中唯一订单号，必填

		// 订单名称
		String subject =trade.getBody();
		// 必填

		// 付款金额
		String total_fee =String.valueOf(trade.getTotalFee());
		// 必填

		// 订单描述

		String body =trade.getBody();
		// 默认支付方式
		String paymethod = "bankPay";
		
		//超时时间
		String itBPay = "1m";
		//选填
		//////////////////////////////////////////////////////////////////////////////////

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
		sParaTemp.put("partner", AliPayProperties.partner);
		sParaTemp.put("seller_email", AliPayProperties.sellerEmail);
		sParaTemp.put("_input_charset", AliPayProperties.charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		//sParaTemp.put("body", body);
		
		sParaTemp.put("paymethod", paymethod);
		sParaTemp.put("defaultbank", trade.getBank());
		//sParaTemp.put("show_url", AliPayProperties.showUrl);
		//sParaTemp.put("it_b_pay", itBPay);
	//	sParaTemp.put("anti_phishing_key", anti_phishing_key);
		//sParaTemp.put("exter_invoke_ip", exter_invoke_ip);

		String requestContent="";
		   for (int i = 0; i < sParaTemp.keySet().size(); i++) {
	            String name = (String) sParaTemp.get(i);
	            String value = (String) sParaTemp.get(i);
	            requestContent+=name+":"+value+"|";
	           
	        }
		
		trade.setRequestContent(requestContent);
		trade.setTradeStatus(PayTask.STATUS_ACCEPTED);

		if (StringUtils.isBlank(trade.getOrderId())) {

			//错误处理
		}
		trade.setRequestTime(java.time.LocalDateTime.now());
		tradeDao.checkOrderStatus(trade);
		if(trade.getTradeStatus()!=-1){//交易表中没订单，则插入订单
		tradeDao.save(trade);
		}
		
		// 建立请求
		String sHtmlText = AliPayHelper.buildRequest(sParaTemp, "get", "ok");
		
		return ResultModel.successModel(sHtmlText);
	}

		public ResultModel sendDataForDirectPay(Trade trade) {

		
		//////////////////////////////////// 请求参数//////////////////////////////////////

		// 支付类型
		String payment_type = "1";
		// 必填，不能修改
		// 服务器异步通知页面路径
		String notify_url = AliPayProperties.domain+AliPayProperties.notify_url_direct;
		// 需http://格式的完整路径，不能加?id=123这类自定义参数

		// 页面跳转同步通知页面路径
		String return_url =AliPayProperties.return_url;
		// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

		// 商户订单号
		String out_trade_no = trade.getOrderId();
		// 商户网站订单系统中唯一订单号，必填

		// 订单名称
		String subject =trade.getBody();
		// 必填

		// 付款金额
		String total_fee =String.valueOf(trade.getTotalFee());
		// 必填

		// 订单描述

		String body =trade.getBody();
	
		//超时时间
		String itBPay = "1m";
		//选填
		//////////////////////////////////////////////////////////////////////////////////

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
		sParaTemp.put("partner", AliPayProperties.partner);
		sParaTemp.put("seller_email", AliPayProperties.sellerEmail);
		sParaTemp.put("_input_charset", AliPayProperties.charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("paymethod", "");
		sParaTemp.put("defaultbank","");
		//sParaTemp.put("body", body);
	
		//sParaTemp.put("show_url", AliPayProperties.showUrl);
		//sParaTemp.put("it_b_pay", itBPay);
	//	sParaTemp.put("anti_phishing_key", anti_phishing_key);
		//sParaTemp.put("exter_invoke_ip", exter_invoke_ip);

		String requestContent="";
		   for (int i = 0; i < sParaTemp.keySet().size(); i++) {
	            String name = (String) sParaTemp.get(i);
	            String value = (String) sParaTemp.get(i);
	            requestContent+=name+":"+value+"|";
	           
	        }
		
		trade.setRequestContent(requestContent);
		trade.setTradeStatus(PayTask.STATUS_ACCEPTED);

		if (StringUtils.isBlank(trade.getOrderId())) {

			//错误处理
		}
		trade.setRequestTime(java.time.LocalDateTime.now());
		tradeDao.checkOrderStatus(trade);
		if(trade.getTradeStatus()!=-1){//交易表中没订单，则插入订单
		tradeDao.save(trade);
		}
		
		// 建立请求
		String sHtmlText = AliPayHelper.buildRequest(sParaTemp, "get", "ok");
		
		return ResultModel.successModel(sHtmlText);
	}

	public ResultModel sendDataForWAPDirectPay(Trade trade) {

		
				//支付类型
				String payment_type = "1";
				// 必填，不能修改
				// 服务器异步通知页面路径
				String notify_url = AliPayProperties.domain+AliPayProperties.notify_url;
				// 需http://格式的完整路径，不能加?id=123这类自定义参数

				// 页面跳转同步通知页面路径
				String return_url =AliPayProperties.m_return_url;
				// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

				//商户订单号
				String out_trade_no = trade.getOrderId();
				//商户网站订单系统中唯一订单号，必填

				//订单名称
				String subject = trade.getBody();
				//必填

				//付款金额
				String total_fee = String.valueOf(trade.getTotalFee());
				//必填

				
				//订单描述
				String body = trade.getBody();
				//选填
				//超时时间
				String itBPay = "1m";

				
				//////////////////////////////////////////////////////////////////////////////////
				
				//把请求参数打包成数组
				Map<String, String> sParaTemp = new HashMap<String, String>();
				sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
		        sParaTemp.put("partner", AliPayProperties.partner);
		        sParaTemp.put("seller_id", AliPayProperties.partner);
		        sParaTemp.put("_input_charset", AliPayProperties.charset);
				sParaTemp.put("payment_type", payment_type);
				sParaTemp.put("notify_url", notify_url);
				sParaTemp.put("return_url", return_url);
				sParaTemp.put("out_trade_no", out_trade_no);
				sParaTemp.put("subject", subject);
				sParaTemp.put("total_fee", total_fee);
				sParaTemp.put("show_url", AliPayProperties.showUrl);
				sParaTemp.put("body", body);
				sParaTemp.put("it_b_pay", itBPay);
				//sParaTemp.put("extern_token", extern_token);
				String requestContent="";
				   for (int i = 0; i < sParaTemp.keySet().size(); i++) {
			            String name = (String) sParaTemp.get(i);
			            String value = (String) sParaTemp.get(i);
			            requestContent+=name+":"+value+"|";
			           
			        }
				
				
				trade.setRequestContent(requestContent);
				trade.setTradeStatus(PayTask.STATUS_ACCEPTED);

				if (StringUtils.isBlank(trade.getOrderId())) {

					//错误处理
				}
				tradeDao.checkOrderStatus(trade);
				if(trade.getTradeStatus()!=-1){//交易表中没订单，则插入订单
				tradeDao.save(trade);
				}
				//建立请求
				String sHtmlText = AliPayHelper.buildRequest(sParaTemp,"get","ok");
				
				return ResultModel.successModel(sHtmlText);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

}
