package spring.boot.pay.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import spring.boot.pay.config.dictionary.Constant;

public class Util {

	// 打log用
	private static Log logger = new Log(LoggerFactory.getLogger(Util.class));

	/**
	 * 通过反射的方式遍历对象的属性和属性值，方便调试
	 *
	 * @param o
	 *            要遍历的对象
	 * @throws Exception
	 */
	public static void reflect(Object o) throws Exception {
		Class cls = o.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			Util.log(f.getName() + " -> " + f.get(o));
		}
	}

	public static byte[] readInput(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		out.close();
		in.close();
		return out.toByteArray();
	}

	public static String inputStreamToString(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	public static InputStream getStringStream(String sInputString) {
		ByteArrayInputStream tInputStringStream = null;
		if (sInputString != null && !sInputString.trim().equals("")) {
			tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
		}
		return tInputStringStream;
	}

	public static String getStringFromMap(Map<String, Object> map, String key, String defaultValue) {
		if (key == "" || key == null) {
			return defaultValue;
		}
		String result = (String) map.get(key);
		if (result == null) {
			return defaultValue;
		} else {
			return result;
		}
	}

	public static int getIntFromMap(Map<String, Object> map, String key) {
		if (key == "" || key == null) {
			return 0;
		}
		if (map.get(key) == null) {
			return 0;
		}
		return Integer.parseInt((String) map.get(key));
	}
	public static String GetImageStr(String path)
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = path;//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try 
        {
            in = new FileInputStream(imgFile);        
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
      
        return Base64.encodeBase64String(data);//返回Base64编码过的字节数组字符串
    }
	/**
	 * 打log接口
	 * 
	 * @param log
	 *            要打印的log字符串
	 * @return 返回log
	 */
	public static String log(Object log) {
		logger.i(log.toString());
		// System.out.println(log);
		return log.toString();
	}

	/**
	 * 读取本地的xml数据，一般用来自测用
	 * 
	 * @param localPath
	 *            本地xml文件路径
	 * @return 读到的xml字符串
	 */
	public static final String getLocalXMLString(final String localPath) throws IOException {
		return Util.inputStreamToString(Util.class.getResourceAsStream(localPath));
	}

	public static final Properties getProperties(final String propertiesPath) {

		 InputStream in= Object.class.getResourceAsStream(propertiesPath);
		Properties properties = new Properties();
		
		if (in!=null) {

			try {
				
				properties.load(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return properties;
	}
	public static final String getFileData(final String path) {

		  InputStream fis= null;
	      int i = 0;
	      char c;
	     
	    
	      try{
	         // create new file input stream
	    	fis=  Object.class.getResourceAsStream(path);
	    	  
	         //fis = new FileInputStream( Thread.currentThread().getContextClassLoader().getResource(path).getPath());
	         byte[] bs = new byte[fis.available()];
	         // read bytes to the buffer
	         fis.read(bs);
	        
	         return  new String(bs,Constant.CHARSET);
	         
	      }catch(Exception ex){
	         // if any error occurs
	         ex.printStackTrace();
	      }finally{
	         
	         // releases all system resources from the streams
	         if(fis!=null){
	            try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	      }
	      
	      return "";
	}
	
	/**
     * 生成二维码图片 不存储 直接以流的形式输出到页面
     * @param content
     * @param response
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void encodeQrcode(String content,HttpServletResponse response){
        if(StringUtils.isBlank(content))
            return;
       MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
       Map hints = new HashMap();
       hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); //设置字符集编码类型
       BitMatrix bitMatrix = null;
       try {
           bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300,hints);
           BufferedImage image = toBufferedImage(bitMatrix);
           //输出二维码图片流
           try {
               ImageIO.write(image, "png", response.getOutputStream());
           } catch (IOException e) {
               // TODO Auto-generated catch block
              logger.e(e.getMessage());
           }
       } catch (WriterException e1) {
           // TODO Auto-generated catch block
    	   logger.e(e1.getMessage());
       }         
   }
    private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

}
