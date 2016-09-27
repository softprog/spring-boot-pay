package spring.boot.pay.model;


public class TradeForPost {

	 
	    //订单号
	    private String orderId;
	    //描述
	    private String body;
	    //货币类型 ,暂时只支持人民
	    private String feeType = "CNY";
	    //订单总金额
	    private double totalFee;
	    private double price;
	    //支付方式(微信支付,支付宝支付)
	    private int payType;
	  
	    private int productId;
	  
	    private int quantity;
	    
	    private String deviceInfo;
	    private int phone;
	   
	  
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		
		
	    public String getOrderId() {
	        return orderId;
	    }
	    public void setOrderId(String orderId) {
	        this.orderId = orderId;
	    }

	   
	    public String getFeeType() {
	        return feeType;
	    }
	    public void setFeeType(String feeType) {
	        this.feeType = feeType;
	    }
	    public double getTotalFee() {
	        return totalFee;
	    }
	    public void setTotalFee(double totalFee) {
	        this.totalFee = totalFee;
	    }
	    
	    public String getBody() {
	        return body;
	    }
	    public void setBody(String body) {
	        this.body = body;
	    }
	    public int getPayType() {
	        return payType;
	    }
	    public void setPayType(int payType) {
	        this.payType = payType;
	    }
	  
		/**
		 * @return the price
		 */
		public double getPrice() {
			return price;
		}
		/**
		 * @param price the price to set
		 */
		public void setPrice(double price) {
			this.price = price;
		}
		/**
		 * @return the deviceInfo
		 */
		public String getDeviceInfo() {
			return deviceInfo;
		}
		/**
		 * @param deviceInfo the deviceInfo to set
		 */
		public void setDeviceInfo(String deviceInfo) {
			this.deviceInfo = deviceInfo;
		}
		
		/**
		 * @return the phone
		 */
		public int getPhone() {
			return phone;
		}
		/**
		 * @param phone the phone to set
		 */
		public void setPhone(int phone) {
			this.phone = phone;
		}
		/**
		 * @return the productId
		 */
		public int getProductId() {
			return productId;
		}
		/**
		 * @param productId the productId to set
		 */
		public void setProductId(int productId) {
			this.productId = productId;
		}
		
		
}
