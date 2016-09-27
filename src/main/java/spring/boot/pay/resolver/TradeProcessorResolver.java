package spring.boot.pay.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spring.boot.pay.config.dictionary.PayType;
import spring.boot.pay.framework.base.PayTask;
import spring.boot.pay.framework.processor.Processor;
import spring.boot.pay.framework.processor.ProcessorManager;
import spring.boot.pay.framework.processor.ProcessorResolver;
import spring.boot.pay.model.Trade;
import spring.boot.pay.processor.TradeResultProcessor;
import spring.boot.pay.thirdparty.alipay.AliPayProcessor;
import spring.boot.pay.thirdparty.wxpay.WxPayProcessor;





@Component
@SuppressWarnings("unused")
public class TradeProcessorResolver implements ProcessorResolver<Trade>{

    @Autowired
    ProcessorManager processorManager;

    @Override
    public Class<? extends Processor> resolve(Trade trade) {

    	 if(trade.getTradeStatus() == PayTask.STATUS_SUCCESS)
             return TradeResultProcessor.class;
        if( trade.getPayType()==PayType.WX_PAY_JSAPI) 
            return WxPayProcessor.class;
        if( trade.getPayType()==PayType.WX_PAY_QC) 
            return WxPayProcessor.class;
        if( trade.getPayType()==PayType.ALI_PAY_NetBank) 
            return AliPayProcessor.class;
        if( trade.getPayType()==PayType.ALI_PAY_WAP) 
           return AliPayProcessor.class;
    
        if( trade.getPayType()==PayType.ALI_PAY_Direct){
            return AliPayProcessor.class;
        }
        return WxPayProcessor.class;
    }

}
