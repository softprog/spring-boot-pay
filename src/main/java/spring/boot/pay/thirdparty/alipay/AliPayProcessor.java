package spring.boot.pay.thirdparty.alipay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import spring.boot.pay.config.dictionary.PayType;
import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.framework.processor.Processor;
import spring.boot.pay.model.Trade;

@Component
@SuppressWarnings("unused")
public class AliPayProcessor implements Processor<Trade> {

	@Autowired
	AliPayService aliPayService;

	@Override
	public ResultModel process(Trade trade) {

		if (trade.getPayType() == PayType.ALI_PAY_NetBank) {

			ResultModel serviceResult = aliPayService.sendDataForDirectPayForbank(trade);
			if (!serviceResult.isSuccess()) {
				return serviceResult;
			}

			return ResultModel.successModel(serviceResult.getBody());
		}
		if (trade.getPayType() == PayType.ALI_PAY_Direct) {

			ResultModel serviceResult = aliPayService.sendDataForDirectPay(trade);
			if (!serviceResult.isSuccess()) {
				return serviceResult;
			}

			return ResultModel.successModel(serviceResult.getBody());
		}
		if (trade.getPayType() == PayType.ALI_PAY_WAP) {

			ResultModel serviceResult = aliPayService.sendDataForWAPDirectPay(trade);
			if (!serviceResult.isSuccess()) {
				return serviceResult;
			}

			return ResultModel.successModel(serviceResult.getBody());
		}
		
		return null;
	}

}
