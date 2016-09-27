package spring.boot.pay.framework.filter;


import com.alibaba.fastjson.JSON;

import spring.boot.pay.common.HttpServRequestHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse)response;
       resp.setHeader("Access-Control-Allow-Origin", "*.zhugexuetang.com");

        long start = System.nanoTime();

        try {
            chain.doFilter(request,response);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        long end = System.nanoTime();

        String url = req.getRequestURL().toString();

        if(logger.isInfoEnabled())
            logger.info("ip:{} , url: {} , time: {} , params:{}",getClientIP(req),url,end-start, JSON.toJSON(HttpServRequestHelper.genSortedMap(req)));

    }

    @Override
    public void destroy() {

    }

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

