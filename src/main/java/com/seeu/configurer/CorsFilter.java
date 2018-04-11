package com.seeu.configurer;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 浏览器访问过滤器，解决跨域名请求等问题
 */
@Component
public class CorsFilter implements Filter {

    private List<String> allowedOrigins = Arrays.asList("http://localhost:8000", "http://localhost:8090", "http://art.seeuio.com:8000", "http://admin.art.seeuio.com", "http://www.vvaryun.com", "http://admin.vvaryun.com", "*");

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "*");
        response.setHeader("Vary", "Origin");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // response.setHeader("Access-Control-Allow-Origin", "*");//解决跨域请求问题
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, PATCH");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}