package spring.boot.pay.thirdparty.wxpay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.framework.processor.Processor;
import spring.boot.pay.model.Trade;



@Component
@SuppressWarnings("unused")
public class WxPayProcessor implements Processor<Trade> {

    @Autowired
    WxPayService wxPayService;

    @Override
    public ResultModel process(Trade trade)  {

        ResultModel serviceResult = wxPayService.getPayJson(trade);
        if( !serviceResult.isSuccess() ){
            return serviceResult;
        }

       
        return ResultModel.successModel(serviceResult.getBody());
    }

}
