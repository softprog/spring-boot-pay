package spring.boot.pay.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.framework.processor.Processor;
import spring.boot.pay.model.Trade;
import spring.boot.pay.service.TradeService;




@Component
@SuppressWarnings("unused")
public class TradeResultProcessor implements Processor<Trade> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TradeService tradeService;
   

    /**
     * 当接收到支付结果通知后  执行一些必要的的操作
     */
    @Override
    public ResultModel process(Trade result){

    	tradeService.updateResult(result);
        ResultModel updateResult =ResultModel.buildModel(true);
        
        return updateResult;		

        
    }
}
