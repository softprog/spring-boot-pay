package spring.boot.pay.thirdparty.wxpay;



import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;

import spring.boot.pay.common.FastJsonUtil;
import spring.boot.pay.common.Util;
import spring.boot.pay.common.encrypt.AliMD5;
import spring.boot.pay.common.encrypt.WXMD5;
import spring.boot.pay.common.http.HttpClientHelper;
import spring.boot.pay.config.dictionary.Constant;
import spring.boot.pay.config.properties.WxPayProperties;


public class WxPayHelper {


    /**
     * 取出一个指定长度大小的随机正整数.
     * @param length int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }


    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return outFormat.format(now);
    }

    /**
     * 获取随机字符串
     */
    public static String getNonceStr() {
        // 随机数
        String currTime = getCurrTime();
        // 8位日期
        String strTime = currTime.substring(8, currTime.length());
        // 四位随机数
        String strRandom = buildRandom(4) + "";
        // 10位序列号,可以自行调整。
        return strTime + strRandom;
    }


    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(SortedMap<String, String> packageParams) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : packageParams.entrySet()) {

            String k = entry.getKey();
            String v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        
      //  String key="1Q2w3e4r5t6y7u8i9Oa1s2d3f4g5h6j7";
        sb.append("key="+WxPayProperties.KEY);
     //  商户key即密钥： 
        return WXMD5.MD5Encode(sb.toString())
                .toUpperCase();

    }

    public static String getRequestXml(SortedMap<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        String sign=WxPayHelper.createSign(parameters);
        
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<").append(k).append("><![CDATA[").append(v)
                        .append("]]></").append(k).append(">");
            } else {
                sb.append("<").append(k).append(">").append(v).append("</")
                        .append(k).append(">");
            }
        }
        
        sb.append("<").append("sign").append("><![CDATA[").append(sign)
        .append("]]></").append("sign").append(">");
        sb.append("</xml>");
        return sb.toString();
    }
    
      public static String authorizeLogin(String oid) {
        
        String state=WXMD5.MD5Encode(String.valueOf(new java.util.Date().getTime()).substring(0, 10));
      
        String urltemplate = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=%s&scope"
                + "=snsapi_base&state=%s#wechat_redirect";
        String url=null;
		try {
			url = String.format(urltemplate, WxPayProperties.APP_ID,
					java.net.URLEncoder.encode(WxPayProperties.DOMAIN+"/pay/tradewxjs/?oid="+oid,"UTF-8"),"code",state);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return url;
      }
}
