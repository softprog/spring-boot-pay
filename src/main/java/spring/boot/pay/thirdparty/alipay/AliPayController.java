package spring.boot.pay.thirdparty.alipay;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.boot.pay.config.properties.AliPayProperties;
import spring.boot.pay.framework.base.PayStatus;
import spring.boot.pay.framework.base.PayTask;
import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.framework.processor.ProcessorManager;
import spring.boot.pay.model.Trade;

@Controller
public class AliPayController {
	@Autowired
	ProcessorManager processorManager;

	@RequestMapping(value = AliPayProperties.notify_url)
	public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		String requestContent = "";
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";

			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);

			requestContent += name + ":" + valueStr + "|";
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号
        if(request.getParameter("out_trade_no")==null){
        	
        	return;
        }
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		// 交易状态
		String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"), "UTF-8");
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		PrintWriter out = response.getWriter();

		if (AliPayHelper.verify(params)) {// 验证成功
			

			Trade payResult = new Trade();
			payResult.setPayStatus(
					"TRADE_SUCCESS".equals(trade_status) ? PayStatus.STATUS_SUCCESS : PayStatus.STATUS_FAIL);
			payResult.setTradeStatus(
					"TRADE_SUCCESS".equals(trade_status) ? PayTask.STATUS_SUCCESS : PayTask.STATUS_FAIL);
			String totalFeeStr = total_fee;
			payResult.setTotalFee(new BigDecimal(totalFeeStr));
			payResult.setOrderId(out_trade_no);
			payResult.setOutTradeId(trade_no);
			payResult.setReturnContent(requestContent);
			payResult.setResultTime(LocalDateTime.now());

			payResult.setSource(2);//来源为阿里银行
			ResultModel processResult = processorManager.process(payResult);

			out.write("sucess");
		} else {// 验证失败
			out.write("failure");
		}

		out.flush();
		out.close();
	}
	
	@RequestMapping(value = AliPayProperties.notify_url_direct)
	public void notifyDirect(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		String requestContent = "";
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";

			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);

			requestContent += name + ":" + valueStr + "|";
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号
		  if(request.getParameter("out_trade_no")==null){
	        	
	        	return;
	        }
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		// 交易状态
		String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"), "UTF-8");
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		PrintWriter out = response.getWriter();

		if (AliPayHelper.verify(params)) {// 验证成功
			

			Trade payResult = new Trade();
			payResult.setPayStatus(
					"TRADE_SUCCESS".equals(trade_status) ? PayStatus.STATUS_SUCCESS : PayStatus.STATUS_FAIL);
			payResult.setTradeStatus(
					"TRADE_SUCCESS".equals(trade_status) ? PayTask.STATUS_SUCCESS : PayTask.STATUS_FAIL);
			String totalFeeStr = total_fee;
			payResult.setTotalFee(new BigDecimal(totalFeeStr));
			payResult.setOrderId(out_trade_no);
			payResult.setOutTradeId(trade_no);
			payResult.setReturnContent(requestContent);
			payResult.setResultTime(LocalDateTime.now());

			payResult.setSource(0);//来源为即时到帐
			ResultModel processResult = processorManager.process(payResult);

			out.write("sucess");
		} else {// 验证失败
			out.write("failure");
		}

		out.flush();
		out.close();
	}
}
