package spring.boot.pay.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import spring.boot.pay.framework.base.PayTask;



/**
 * 支付model类
 */
public class Trade implements PayTask{

    private Integer id;
    //订单号
    private String orderId;
    //描述
    private String body;
    //货币类型 ,暂时只支持人民
    private String feeType = "CNY";
    //订单总金额
    private BigDecimal totalFee;
    private double price;
    //支付方式(微信支付,支付宝支付)
    private int payType;
    //如使用第三方支付时的一些标示,只有使用特殊的支付方式时才需要
    private String openId;
    //如使用第三方支付时的一些标示,只有使用特殊的支付方式时才需要
    private String code;
    //支付完成后的返回页面，返回时将携带支付结果
    private String returnUrl;
   
    //第三方(微信,支付宝)的交易号
    private String outTradeId;

    //发起请求的时间
    private LocalDateTime requestTime;
    //获得结果的时间
    private LocalDateTime resultTime;
    
    //任务状态
    private int tradeStatus = 1;//1 成功 0 失败
    
    //调用端ip
    private String ip="";
    private int productId;
    private String productType;
    private int orderType;
    private int quantity;
    private String requestContent;
    private String returnContent;
    private String pageReturnContent;
    private String deviceInfo;
    private long phone;
    private int payStatus;
    private String finishTime;
    private String bank;
    /**
     * 0支付宝   1 微信 
     */
    private int source;
    
    private String coupon;
    
    private String orgName;
    
    public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getReturnUrl() {
        return returnUrl;
    }
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
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
    public BigDecimal getTotalFee() {
        return totalFee;
    }
    public void setTotalFee(BigDecimal totalFee) {
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
   
   
    public String getOutTradeId() {
        return outTradeId;
    }
    public void setOutTradeId(String outTradeId) {
        this.outTradeId = outTradeId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
   

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getResultTime() {
        return resultTime;
    }

    public void setResultTime(LocalDateTime resultTime) {
        this.resultTime = resultTime;
    }
	public int getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getRequestContent() {
		return requestContent;
	}
	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}
	/**
	 * @return the returnContent
	 */
	public String getReturnContent() {
		return returnContent;
	}
	/**
	 * @param returnContent the returnContent to set
	 */
	public void setReturnContent(String returnContent) {
		this.returnContent = returnContent;
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
	 * @return the pageReturnContent
	 */
	public String getPageReturnContent() {
		return pageReturnContent;
	}
	/**
	 * @param pageReturnContent the pageReturnContent to set
	 */
	public void setPageReturnContent(String pageReturnContent) {
		this.pageReturnContent = pageReturnContent;
	}
	/**
	 * @return the phone
	 */
	public long getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(long phone) {
		this.phone = phone;
	}
	/**
	 * @return the payStatus
	 */
	public int getPayStatus() {
		return payStatus;
	}
	/**
	 * @param payStatus the payStatus to set
	 */
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public int getSource() {
		return source;
	}
	/**
	 *
	 * @param source  0支付宝   1 微信 
	 */
	public void setSource(int source) {
		this.source = source;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
