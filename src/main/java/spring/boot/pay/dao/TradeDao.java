package spring.boot.pay.dao;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import spring.boot.pay.model.Trade;

@Repository
public class TradeDao extends BaseDao {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 保存交易请求
	 * 
	 * @param trade
	 *            交易请求
	 * @return 成功返回交易请求的id,保存失败返回-1
	 */
	public int save(Trade trade) {

		 throw new UnsupportedOperationException();

	}

	/**
	 * 更新交易的结果
	 * 
	 * @param trade
	 *            交易结果
	 */
	public void updateReturnContent(Trade trade) {

		 throw new UnsupportedOperationException();
	}

	/**
	 * 更新交易的结果
	 * 
	 * @param trade
	 *            交易结果
	 */
	public void updateResult(Trade trade) {
		 throw new UnsupportedOperationException();
	}

	/**
	 * 更新交易的状态,用于交易完成后
	 * 
	 * @param trade
	 *            交易的状态
	 */
	public void updateStatus(int status, String orderId) {

		 throw new UnsupportedOperationException();
	}
	
	/**
	 * 查询订单号是否在交易中，以及状态
	 */
	public Trade checkOrderStatus(Trade trade) {
		 throw new UnsupportedOperationException();
	}
	
	public BigDecimal getOrderTotalPrice(String orderId) {
		 throw new UnsupportedOperationException();
	}
	
	
}