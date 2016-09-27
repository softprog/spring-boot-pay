package spring.boot.pay.model;

import java.math.BigDecimal;

//endPtime,sellPrice,buyPrice
public class KeInfo {

	private int kid=0;
	private long endPtime;
	private BigDecimal backPlayPrice;
	private BigDecimal buyPrice;
	
	public int getKid() {
		return kid;
	}
	public void setKid(int kid) {
		this.kid = kid;
	}
	public long getEndPtime() {
		return endPtime;
	}
	public void setEndPtime(long endPtime) {
		this.endPtime = endPtime;
	}
	
	public BigDecimal getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}
	public BigDecimal getBackPlayPrice() {
		return backPlayPrice;
	}
	public void setBackPlayPrice(BigDecimal backPlayPrice) {
		this.backPlayPrice = backPlayPrice;
	}
}
