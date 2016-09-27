package spring.boot.pay.thirdparty.wxpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.boot.pay.common.HttpServRequestHelper;
import spring.boot.pay.common.XmlHelper;
import spring.boot.pay.config.dictionary.PayType;
import spring.boot.pay.config.properties.WxPayProperties;
import spring.boot.pay.framework.base.PayStatus;
import spring.boot.pay.framework.base.PayTask;
import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.framework.processor.ProcessorManager;
import spring.boot.pay.framework.security.CheckIpEndpoint;
import spring.boot.pay.model.Trade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.SortedMap;

@Controller
@SuppressWarnings("unused")
public class WxController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxController.class);

    private static final String WX_RESPONSE_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[接收通知成功]]></return_msg></xml> ";
    private static final String WX_RESPONSE_FAIL = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[接收通知失败]]></return_msg></xml> ";

  
    @Autowired WxPayService wxPayService;
    
    @Autowired
    ProcessorManager processorManager;
    /**
     * 接收微信通知
     * <xml>
     *     <appid><![CDATA[wxf088cc8f4c48f416]]></appid>
     *     <bank_type><![CDATA[CCB_DEBIT]]></bank_type>
     *     <cash_fee><![CDATA[200]]></cash_fee>
     *     <fee_type><![CDATA[CNY]]></fee_type>
     *     <is_subscribe><![CDATA[Y]]></is_subscribe>
     *     <mch_id><![CDATA[1251923601]]></mch_id>
     *     <nonce_str><![CDATA[1602133769]]></nonce_str>
     *     <openid><![CDATA[ojS7osoSFRzwFrcbYLZUUPAGEfhw]]></openid>
     *     <out_trade_no><![CDATA[1744821]]></out_trade_no>
     *     <result_code><![CDATA[SUCCESS]]></result_code>
     *     <return_code><![CDATA[SUCCESS]]></return_code>
     *     <sign><![CDATA[19913E49A1830E1C7077E05261314B84]]></sign>
     *     <time_end><![CDATA[20150918160220]]></time_end>
     *     <total_fee>200</total_fee>
     *     <trade_type><![CDATA[JSAPI]]></trade_type>
     *     <transaction_id><![CDATA[1005790427201509180917373724]]></transaction_id>
     * </xml>
     */
    @RequestMapping(value = WxPayProperties.NOTIFY_URL)
    public void notify(HttpServletRequest request,HttpServletResponse response) throws Exception {

        String notifyXml = HttpServRequestHelper.getRequestBody(request);

        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("received WeChat notify,notify context is {}", notifyXml);
        }

        PrintWriter out = response.getWriter();

        Trade payResult = buildPayResult(notifyXml);
        
        if(payResult == null){
            out.write(WX_RESPONSE_FAIL);
        } else {
        	
        	payResult.setSource(1);
            ResultModel processResult = processorManager.process(payResult);
            if(payResult.getTradeStatus()==PayTask.STATUS_SUCCESS){
                out.write(WX_RESPONSE_SUCCESS);
            }else {
                out.write(WX_RESPONSE_FAIL);
            }
        }

        out.flush();
        out.close();
    }


    private Trade buildPayResult(String notifyXml){

        SortedMap<String,String> map = XmlHelper.parseXmlToMap(notifyXml);
        if( map == null ){
            LOGGER.warn("can not parse the notify params,the notify xml={}", notifyXml);
            return null;
        }

        String sign = WxPayHelper.createSign(map);
        if( !sign.equalsIgnoreCase(map.get("sign")) ){
            LOGGER.warn("check params error,the notify xml is:" + notifyXml+";the sign of mine is:"+sign);
            return null;
        }

        Trade payResult = new Trade();
        payResult.setPayStatus("SUCCESS".equals(map.get("result_code")) ? PayStatus.STATUS_SUCCESS : PayStatus.STATUS_FAIL);
        payResult.setTradeStatus("SUCCESS".equals(map.get("result_code")) ? PayTask.STATUS_SUCCESS : PayTask.STATUS_FAIL);
        String totalFeeStr = map.get("total_fee");
        payResult.setTotalFee(new BigDecimal(totalFeeStr));
        payResult.setOrderId(map.get("out_trade_no"));
        payResult.setOutTradeId(map.get("transaction_id"));
        payResult.setReturnContent(notifyXml);
        payResult.setResultTime(LocalDateTime.now());
        
        return payResult;
    }
    
   
}
