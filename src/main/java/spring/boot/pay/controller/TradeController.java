package spring.boot.pay.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.xml.internal.messaging.saaj.util.Base64;

import spring.boot.pay.common.Util;
import spring.boot.pay.common.encrypt.ProtectData;
import spring.boot.pay.config.dictionary.Constant;
import spring.boot.pay.config.dictionary.PayType;
import spring.boot.pay.config.properties.WxPayProperties;
import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.model.OrderInfo;
import spring.boot.pay.model.Trade;
import spring.boot.pay.service.TradeService;
import spring.boot.pay.thirdparty.wxpay.WxPayHelper;

@Controller
@SuppressWarnings("unused")
public class TradeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);
	// 发起支付的时间离订单过期时间至少相差5分钟
	private static final int FIVE_MINUTE = 5 * 1000 * 60;

	@Autowired
	TradeService tradeService;

	/**
	 * 解析支付url得到支付请求信息 进行支付
	 */
	@RequestMapping(value = "/pay/trade", method = RequestMethod.POST)
	@ResponseBody
	// @CheckIpEndpoint
	public String trade(HttpServletRequest request, HttpServletResponse response, String data) throws Exception {

		ValidateResult result = this.validateTradeDataForWx(data, request);

		if (result.getTrade() == null) {

			return result.getResult();
		}

		ResultModel processResult = this.tradeService.doTrade(result.getTrade());

		if (!processResult.isSuccess()) {
			return ProtectData.AESEncrypt(Constant.AESkeyStr, toFailureJson(processResult.getBody().toString()));
		}

		return ProtectData.AESEncrypt(Constant.AESkeyStr, toSuccessJson(processResult.getBody().toString()));

	}
	
	/**
	 * 解析支付url得到支付请求信息 进行支付
	 */
	@RequestMapping(value = "/pay/tradewxlogin", method = RequestMethod.GET)
	@ResponseBody
   
	public void tradeforwxlogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String oid = request.getParameter("oid");
		response.sendRedirect(WxPayHelper.authorizeLogin(oid));
	}

	/**
	 * 解析支付url得到支付请求信息 进行支付
	 */
	@RequestMapping(value = "/pay/tradewxjs", method = RequestMethod.GET)
	@ResponseBody
	// @CheckIpEndpoint
	public void tradeWXJS(HttpServletRequest request, HttpServletResponse response, String data) throws Exception {

		
		ValidateResult result = this.validateTradeDataForWxjs(data,request);

		if (result.getTrade() == null) {

			return;
		}

		
		ResultModel processResult = this.tradeService.doTrade(result.getTrade());
		// ResultModel processResult =ResultModel.successModel("32343");
		if (!processResult.isSuccess()) {
			return;
		}

	
		
	
		 response.setContentType("text/html; charset=UTF-8");
		 response.setCharacterEncoding("UTF-8"); String
		 jsapiData=handleJSAPI(processResult.getBody().toString());
		 PrintWriter out = response.getWriter(); 
		  out.print(jsapiData);
		  out.close();
		 //response.getOutputStream().print(new String(jsapiData.getBytes(),
		//  "UTF-8"));
		 //response.getOutputStream().flush();
		 // response.getOutputStream().close();
		
		  return;

	}
	/**
	 * 解析支付url得到支付请求信息 进行支付
	 */
	@RequestMapping(value = "/pay/tradealibank", method = RequestMethod.GET)
	@ResponseBody
	// @CheckIpEndpoint
	public void tradeAliBank(HttpServletRequest request, HttpServletResponse response, String data) throws Exception {

		ValidateResult result = this.validateTradeDataForAliBank(data,request);

		if (result.getTrade() == null) {

			return;
		}

		
		ResultModel processResult = this.tradeService.doTrade(result.getTrade());
		// ResultModel processResult =ResultModel.successModel("32343");
		if (!processResult.isSuccess()) {
			return;
		}

		// if (trade.getPayType() == PayType.ALI_PAY_NetBank ||
		// trade.getPayType() == PayType.ALI_PAY_WAP) {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new String(processResult.getBody().toString().getBytes(), "UTF-8"));
		// response.getOutputStream().flush();
		// response.getOutputStream().close();
		// }

		/*
		 * if (trade.getPayType() == PayType.WX_PAY_JSAPI) {
		 * response.setContentType("text/html; charset=UTF-8");
		 * response.setCharacterEncoding("UTF-8"); String
		 * jsapiData=handleJSAPI(processResult.getBody().toString());
		 * 
		 * response.getOutputStream().print(new String(jsapiData.getBytes(),
		 * "UTF-8")); response.getOutputStream().flush();
		 * response.getOutputStream().close();
		 * 
		 * }
		 */

		return;

	}

	/**
	 * 解析支付url得到支付请求信息 进行支付
	 */
	@RequestMapping(value = "/pay/tradealidirect", method = RequestMethod.GET)
	@ResponseBody
	// @CheckIpEndpoint
	public void tradeAliDirect(HttpServletRequest request, HttpServletResponse response, String data) throws Exception {

		ValidateResult result =  this.validateTradeDataForAliDirect(data,request);

		if (result.getTrade() == null) {

			return;
		}
		
		ResultModel processResult = this.tradeService.doTrade(result.getTrade());
		
		if (!processResult.isSuccess()) {
			return;
		}

	
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		LOGGER.error("ALI_PAY_Direct exec here");
		System.out.println("ALI_PAY_Direct exec here");
		response.getWriter().write(new String(processResult.getBody().toString().getBytes(), "UTF-8"));
		LOGGER.error("ALI_PAY_Direct exec ok");
		System.out.println("ALI_PAY_Direct exec ok");
		
		return;

	}

	/**
	 * 解析支付url得到支付请求信息 进行支付
	 */
	@RequestMapping(value = "/pay/tradealiwap", method = RequestMethod.GET)
	@ResponseBody
	// @CheckIpEndpoint
	public void tradeAliWAP(HttpServletRequest request, HttpServletResponse response, String data) throws Exception {

		ValidateResult result =  this.validateTradeDataForAliWAP(data,request);

		if (result.getTrade() == null) {

			return;
		}
		
		ResultModel processResult = this.tradeService.doTrade(result.getTrade());
		
		if (!processResult.isSuccess()) {
			return;
		}

	
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		LOGGER.error("tradealiwap exec here");
		System.out.println("tradealiwap exec here");
		response.getWriter().write(new String(processResult.getBody().toString().getBytes(), "UTF-8"));
		LOGGER.error("tradealiwap exec ok");
		System.out.println("tradealiwap exec ok");
		
		return;

	}

	/**
	 * 生成二维码图片并直接以流的形式输出到页面
	 * 
	 * @param code_url
	 * @param response
	 */
	@RequestMapping("qr_code.img")
	@ResponseBody
	public void getQRCode(String code_url, HttpServletResponse response) {
		Util.encodeQrcode(code_url, response);
	}

	private String toSuccessJson(final String msg) {
		return "{\"returnCode\":1,\"code\":0,\"body\":" + msg + "}";
	}

	private String toFailureJson(final String msg) {
		return "{\"returnCode\":0,\"code\":1,\"body\":" + msg + "}";
	}

	private String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {

			ip = "127.0.0.1";
		}
		return ip == null ? "" : ip;
	}

	private static final String handleJSAPI(String data) {
		
		SortedMap<String, String> params = new TreeMap<String, String>();
		String timeStamp = String.valueOf(new java.util.Date().getTime()).substring(0, 10);
		String nonceStr = WxPayHelper.getNonceStr();
		params.put("appId", WxPayProperties.APP_ID);
		params.put("timeStamp", timeStamp);
		params.put("nonceStr", nonceStr);
		params.put("package","prepay_id=" + data);
		params.put("signType", "MD5");
		String sign = WxPayHelper.createSign(params);

		return WxPayProperties.jsapiData.replace("$appId", WxPayProperties.APP_ID).replace("$timeStamp", timeStamp)
				.replace("$nonceStr", nonceStr).replace("$prepayId", "prepay_id=" + data).replace("$paySign", sign)
				.replace("$url", "http://m.zhugexuetang.cn");
	}

	private String handleData(String data) {

		data = Base64.base64Decode(data);

		String dec = ProtectData.AESDecrypt(Constant.AESkeyStr, data);

		return dec;
	}

	private final ValidateResult validateTradeDataForWx(String data, HttpServletRequest request) {

		ValidateResult result = new ValidateResult();
		result.setTrade(null);
		result.setResult(toFailureJson("ok"));
		String dec = this.handleData(data);
		if (dec.equals("")) {

			result.setResult(toFailureJson("解密失败"));

			return result;
		}

		String[] tradeParam = dec.split("&");
		Trade trade = new Trade();
		trade.setBody(tradeParam[1].split("=")[1]);
		if (trade.getBody().length() > 20) {
			trade.setBody(trade.getBody().substring(0, 20) + "...");
		}
		trade.setOrderId(tradeParam[0].split("=")[1]);
		trade.setIp(this.getClientIP(request));
		trade.setPayType(PayType.WX_PAY_QC);
		
		String orderId = trade.getOrderId();

		OrderInfo info = tradeService.getOrderInfo(orderId);

		if (info.getNum() == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}

		if (info.getState()) {

			result.setResult(toFailureJson("订单已经支付成功"));

			return result;
		}

		trade.setTotalFee(info.getTotalFee());

		if (trade.getTotalFee().compareTo(BigDecimal.ZERO) == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}
		
		
		result.setTrade(trade);
		
		return result;
	}
	private final ValidateResult validateTradeDataForWxjs(String data, HttpServletRequest request) {

		ValidateResult result = new ValidateResult();
		result.setTrade(null);
		result.setResult(toFailureJson("ok"));
		String dec = this.handleData(data);
		if (dec.equals("")) {

			result.setResult(toFailureJson("解密失败"));

			return result;
		}

		String[] tradeParam = dec.split("&");
		Trade trade = new Trade();
		trade.setBody(tradeParam[2].split("=")[1]);
		if (trade.getBody().length() > 20) {
			trade.setBody(trade.getBody().substring(0, 20) + "...");
		}
		trade.setOrderId(tradeParam[0].split("=")[1]);
		trade.setIp(this.getClientIP(request));
		trade.setPayType(PayType.WX_PAY_JSAPI);
		
		trade.setOpenId(tradeParam[1].split("=")[1]);
		
		String orderId = trade.getOrderId();

		OrderInfo info = tradeService.getOrderInfo(orderId);

		if (info.getNum() == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}

		if (info.getState()) {

			result.setResult(toFailureJson("订单已经支付成功"));

			return result;
		}

		trade.setTotalFee(info.getTotalFee());
		
		if (trade.getTotalFee().compareTo(BigDecimal.ZERO) == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}
		
	
		result.setTrade(trade);
		return result;
	}

	private final ValidateResult validateTradeDataForAliBank(String data, HttpServletRequest request) {

		ValidateResult result = new ValidateResult();
		result.setTrade(null);
		result.setResult(toFailureJson("ok"));
		String dec = this.handleData(data);
		if (dec.equals("")) {

			result.setResult(toFailureJson("解密失败"));

			return result;
		}

		String[] tradeParam = dec.split("&");
		Trade trade = new Trade();
		trade.setBody(tradeParam[2].split("=")[1]);
		if (trade.getBody().length() > 20) {
			trade.setBody(trade.getBody().substring(0, 20) + "...");
		}
		
		trade.setOrderId(tradeParam[0].split("=")[1]);
			trade.setIp(this.getClientIP(request));
		trade.setPayType(PayType.ALI_PAY_NetBank);
		trade.setBank(tradeParam[1].split("=")[1]);
			String orderId = trade.getOrderId();

		OrderInfo info = tradeService.getOrderInfo(orderId);

		if (info.getNum() == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}

		if (info.getState()) {

			result.setResult(toFailureJson("订单已经支付成功"));

			return result;
		}
        trade.setTotalFee(info.getTotalFee());
		
		if (trade.getTotalFee().compareTo(BigDecimal.ZERO) == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}
		
		result.setTrade(trade);
		return result;
	}

	private final ValidateResult validateTradeDataForAliDirect(String data, HttpServletRequest request) {

		ValidateResult result = new ValidateResult();
		result.setTrade(null);
		result.setResult(toFailureJson("ok"));
		String dec = this.handleData(data);
		if (dec.equals("")) {

			result.setResult(toFailureJson("解密失败"));

			return result;
		}

		String[] tradeParam = dec.split("&");
		Trade trade = new Trade();
		trade.setBody(tradeParam[1].split("=")[1]);
		if (trade.getBody().length() > 20) {
			trade.setBody(trade.getBody().substring(0, 20) + "...");
		}
	
		
		trade.setOrderId(tradeParam[0].split("=")[1]);
		trade.setIp(this.getClientIP(request));
		trade.setPayType(PayType.ALI_PAY_Direct);
		
		String orderId = trade.getOrderId();

		OrderInfo info = tradeService.getOrderInfo(orderId);

		if (info.getNum() == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}

		if (info.getState()) {

			result.setResult(toFailureJson("订单已经支付成功"));

			return result;
		}

		trade.setTotalFee(info.getTotalFee());
		
		if (trade.getTotalFee().compareTo(BigDecimal.ZERO) == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}
		
		
		result.setTrade(trade);
		return result;
	}
	private final ValidateResult validateTradeDataForAliWAP(String data, HttpServletRequest request) {

		ValidateResult result = new ValidateResult();
		result.setTrade(null);
		result.setResult(toFailureJson("ok"));
		String dec = this.handleData(data);
		if (dec.equals("")) {

			result.setResult(toFailureJson("解密失败"));

			return result;
		}

		String[] tradeParam = dec.split("&");
		Trade trade = new Trade();
		trade.setBody(tradeParam[1].split("=")[1]);
		if (trade.getBody().length() > 20) {
			trade.setBody(trade.getBody().substring(0, 20) + "...");
		}
		
		trade.setOrderId(tradeParam[0].split("=")[1]);
			trade.setIp(this.getClientIP(request));
		trade.setPayType(PayType.ALI_PAY_WAP);
		String orderId = trade.getOrderId();

		OrderInfo info = tradeService.getOrderInfo(orderId);

		if (info.getNum() == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}

		if (info.getState()) {

			result.setResult(toFailureJson("订单已经支付成功"));

			return result;
		}

		trade.setTotalFee(info.getTotalFee());
		
		if (trade.getTotalFee().compareTo(BigDecimal.ZERO) == 0) {

			result.setResult(toFailureJson("获取订单表订单失败"));

			return result;
		}
		
		
		result.setTrade(trade);
		return result;
	}

	private class ValidateResult {

		public Trade getTrade() {
			return trade;
		}

		public void setTrade(Trade trade) {
			this.trade = trade;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		private Trade trade;
		private String result;
	}
}