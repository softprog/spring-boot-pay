package spring.boot.pay.common.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spring.boot.pay.config.dictionary.Constant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

public class RSA{
	
	public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final Logger LOGGER = LoggerFactory.getLogger(RSA.class);

	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 商户私钥
	* @return 签名值
	*/
	public static String sign(String content,String privateKey)
	{
        try 
        {
        	PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec( Base64.decodeBase64(privateKey) );
        	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        	PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update( content.getBytes(Constant.CHARSET) );

            byte[] signed = signature.sign();
            
            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
        	LOGGER.error(e.getMessage(),e);
        }
        
        return null;
    }
	
	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param publicKey 公钥
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] encodedKey = Base64.decodeBase64(publicKey);
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(Constant.CHARSET));
			return signature.verify( Base64.decodeBase64(sign) );

		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		return false;
	}
	
	/**
	* 解密
	* @param content 密文
	* @param privateKey 商户私钥
	* @param input_charset 编码格式
	* @return 解密后的字符串
	*/
	public static String decrypt(String content, String privateKey, String input_charset) throws Exception {
        PrivateKey key = getPrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        InputStream ins = new ByteArrayInputStream(Base64.decodeBase64(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();

        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufLength;
        while ((bufLength = ins.read(buf)) != -1) {
            if (buf.length == bufLength) {
				writer.write(cipher.doFinal(buf));
            } else {
				writer.write(cipher.doFinal(Arrays.copyOf(buf,bufLength)));
            }
        }
        return new String(writer.toByteArray(), input_charset);
    }

	
	/**
	* 得到私钥
	* @param key 密钥字符串（经过base64编码）
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes = Base64.decodeBase64(key);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		return keyFactory.generatePrivate(keySpec);
	}
}
