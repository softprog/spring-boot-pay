package spring.boot.pay.config.properties;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spring.boot.pay.common.Util;

public class JdbcProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcProperties.class);

    public static String driverClass = "";
    public static String jdbcUrl = "";
    public static String userName = "";
    public static String password = "";
    public static String jdbcUrl_order = "";
    public static String userName_order = "";
    public static String password_order = "";
    public static int maxActive = 0;


    public static void init() throws IOException {
        
            Properties properties =Util.getProperties("/config/db.properties");
           
           
            jdbcUrl = properties.getProperty("url");
            userName = properties.getProperty("username");
            password = properties.getProperty("password");
            driverClass = properties.getProperty("driverClass");
            if(properties.getProperty("maxActive") != null){
                maxActive = Integer.parseInt( properties.getProperty("maxActive") );
            }
            jdbcUrl_order = properties.getProperty("url_order");
            userName_order = properties.getProperty("username_order");
            password_order = properties.getProperty("password_order");
    }
}
