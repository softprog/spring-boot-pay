package spring.boot.pay.common.encrypt;

/**
 * 
 * @author chenlei
 *
 *根据指定的加密方式，对数据进行加解密
 */
public  final  class ProtectData {

	public static final String AESEncrypt(String key,String data){
		
		return  CryptAES.AES_Encrypt(key, data);
	}
	
	public static final String AESDecrypt(String key,String data){
		
		return  CryptAES.AES_Decrypt(key, data);
	}
}
