package spring.boot.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import spring.boot.pay.config.properties.AliPayProperties;
import spring.boot.pay.config.properties.JdbcProperties;
import spring.boot.pay.config.properties.SecurityProperties;
import spring.boot.pay.config.properties.WxPayProperties;


@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})

public class PayInterface{

    private static Logger logger = LoggerFactory.getLogger(PayInterface.class);

    public static void main(String[] args) throws Exception {

        initConfig();
       
        logger.info("Config加载完毕~");//
      
        
      SpringApplication.run(PayInterface.class, args);
    }

    public static void initConfig() throws Exception{
       
        SecurityProperties.init();
        JdbcProperties.init();
        WxPayProperties.init();
        AliPayProperties.init();
    }

 

}
