package spring.boot.pay.model;

import java.math.BigDecimal;

public class OrderInfo {

	public int num;
	public boolean state;
	public BigDecimal totalFee;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	
}
