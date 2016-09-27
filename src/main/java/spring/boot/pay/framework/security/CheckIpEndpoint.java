package spring.boot.pay.framework.security;

import java.lang.annotation.*;

/**
 * 加上该注解后  只有白名单中的ip请求才可以访问此方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckIpEndpoint {
}
