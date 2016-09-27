package spring.boot.pay.framework.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import spring.boot.pay.config.properties.SecurityProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


@Configuration
@Aspect
public class SecurityManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityManager.class);

    //IP白名单
    private Set<String> whiteList = SecurityProperties.getWhiteList();

    @Around("@annotation(zw.app.classquality.pay.framework.security.CheckIpEndpoint)")
    public Object checkIP(ProceedingJoinPoint point) throws Throwable {
        for (Object arg : point.getArgs()) {
            if ( arg instanceof HttpServletRequest ) {
                HttpServletRequest request = (HttpServletRequest) arg;
                String ip = getClientIP(request);
                if ( whiteList.contains(ip)) {
                    return point.proceed();
                }
                LOGGER.warn("illegal ip:{} try to visit {}",ip,request.getRequestURI());
                return null;
            }
        }
        throw new RuntimeException("the method which use @CheckIpEndpoint must has a HttpServletRequest as Parameter");
    }


//    @Around("@annotation()")
//    public Object checkSign(ProceedingJoinPoint point) throws Throwable {
//        for (Object arg : point.getArgs()) {
//            if ( arg instanceof HttpServletRequest ) {
//                HttpServletRequest request = (HttpServletRequest) arg;
//
//                return null;
//            }
//        }
//        throw new RuntimeException("the method which use @CheckSignEndpoint must has a HttpServletRequest as Parameter");
//    }
//
//
//    /**
//     * 检查签名
//     */
//    public ResultModel checkSign(HttpServletRequest request){
//
//        String accountType = request.getParameter("accountType");
//        String sign = request.getParameter("sign");
//
//        if(accountType == null || sign == null || (!accountType.matches("\\d{1,3}")) ){
//            return ResultModel.failModel();
//        }
//
//        SignBuilder signBuilder = new SignBuilder(partnerCache.getSecret(Integer.parseInt(accountType)));
//        Enumeration<String> parameterNames = request.getParameterNames();
//        while ( parameterNames.hasMoreElements() ){
//            String key = parameterNames.nextElement();
//            signBuilder.append(key,request.getParameter(key));
//        }
//        String realSign = signBuilder.build();
//
//        if(sign.equalsIgnoreCase(realSign)){
//            return ResultModel.successModel();
//        }
//
//        return ResultModel.failModel();
//    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip==null?"":ip;
    }

}
