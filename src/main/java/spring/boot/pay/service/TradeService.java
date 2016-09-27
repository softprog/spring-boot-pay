package spring.boot.pay.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spring.boot.pay.dao.TradeDao;
import spring.boot.pay.framework.base.ResultModel;
import spring.boot.pay.framework.processor.ProcessorManager;
import spring.boot.pay.model.OrderInfo;
import spring.boot.pay.model.Trade;



@Component
public class TradeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    TradeDao tradeDao;
    @Autowired
    ProcessorManager processorManager;
   
    public ResultModel doTrade(Trade trade){
        return processorManager.process(trade);
    }
    
    public void updateResult(Trade trade){
    	tradeDao.updateResult(trade);
    	
    }
 
    /**
    * 查询订单号是否存在，以及状态等
    */
   public OrderInfo getOrderInfo(String orderId){
      
	   throw new UnsupportedOperationException();
   }
  
	/**
	 * 查询订单号是否在交易中，以及状态
	 */
	public BigDecimal getOrderTotalPrice(String orderId) {
		
		return tradeDao.getOrderTotalPrice(orderId);
	}
  
}
