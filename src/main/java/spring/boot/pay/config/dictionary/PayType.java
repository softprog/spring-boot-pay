package spring.boot.pay.config.dictionary;

/**
 * Created by chenlei on 15/12/15.
 */
public class   PayType {



    /**
     * 微信公众号支付
     */
    public static final int WX_PAY_JSAPI = 1;
    
    /**
     * 微信扫码支付
     */
    public static final int WX_PAY_QC = 2;
    
    /**
     * 阿里银联支付
     */
    public static final int ALI_PAY_WAP = 3;
    
    /**
     * 阿里手机网站支付
     */
    public static final int ALI_PAY_NetBank =4;
    
    /**
     * 阿里直接支付
     */
    public static final int ALI_PAY_Direct =5;
}
