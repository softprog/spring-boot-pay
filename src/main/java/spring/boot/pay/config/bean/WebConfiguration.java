package spring.boot.pay.config.bean;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import spring.boot.pay.config.dictionary.Constant;

import javax.servlet.Filter;
import java.io.FileNotFoundException;
import java.util.Locale;

@Configuration
public class WebConfiguration {


    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() throws FileNotFoundException {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/html/404.html"));

//                if(container instanceof TomcatEmbeddedServletContainerFactory) {
//                    TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) container;
//                    containerFactory.addAdditionalTomcatConnectors(createSslConnector("/app/paycenter/ssl.p12", "lys418754658", "PKCS12",null));
//                }
            }
        };
    }

//    private Connector createSslConnector(String absoluteKeystoreFile,
//                                         String keystorePassword, String keystoreType, String keystoreAlias) {
//
//        Connector connector = new Connector();
//        connector.setPort(8443);
//        connector.setSecure(true);
//        connector.setScheme("https");
//        connector.setAttribute("SSLEnabled", true);
//        connector.setAttribute("sslProtocol", "TLS");
//        connector.setAttribute("protocol", "org.apache.coyote.http11.Http11Protocol");
//        connector.setAttribute("clientAuth", false);
//        connector.setAttribute("keystoreFile", absoluteKeystoreFile);
//        connector.setAttribute("keystoreType", keystoreType);
//        connector.setAttribute("keystorePass", keystorePassword);
//        if(keystoreAlias != null) {
//            connector.setAttribute("keystoreAlias", keystoreAlias.toLowerCase());
//        }
//        connector.setAttribute("keyPass", keystorePassword);
//
//        return connector;
//    }

    @Bean
    public ErrorController errorController(){
        return new ErrorController() {
            @Override
            public String getErrorPath() {
                return "/html/error.html";
            }
        };
    }


    @Bean
    public ViewResolver jsonViewResolver(){
        return new ViewResolver() {
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            @Override
            public View resolveViewName(String viewName, Locale locale) throws Exception {
                if(viewName.equals("jsonView")) {
                    return view;
                }
                return null;
            }
        };
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(Constant.CHARSET);
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }


//    @Component
//    public static class SasAllowOriginInterceptor extends HandlerInterceptorAdapter {
//
//        @Override
//        public boolean preHandle(HttpServletRequest request,
//                                 HttpServletResponse response, Object handler)
//                throws Exception {
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods",
//                    "GET, POST, PUT, DELETE, OPTIONS");
//            return true;
//        }
//    }

}
