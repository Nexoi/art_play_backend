package com.seeu.configurer;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.seeu.artshow.record.model.UserRecordCache;
import com.seeu.artshow.record.service.RecordService;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.utils.AppAuthFlushService;
import com.seeu.artshow.utils.jwt.JwtUtil;
import com.seeu.artshow.utils.jwt.PhoneCodeToken;
import com.seeu.core.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AppAuthFlushService appAuthFlushService;
    @Autowired
    private RecordService recordService;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //定义一个转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect); // 不做循环检测
        //在转换器中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //将转换器添加到converters中
        converters.add(fastConverter);
    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new HandlerInterceptorAdapter() {

            private void recordUser(String sessionId, boolean isLogin) {
                recordService.recordUser(
                        sessionId,
                        isLogin
                                ? UserRecordCache.TYPE.REGISTED
                                : UserRecordCache.TYPE.NICK);
            }

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                System.out.println("SESSION ID >> " + request.getRequestedSessionId());
                request.getSession().setAttribute("loginSuccessURL", request.getHeader("referer"));
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal != null && principal instanceof UserDetails) {
                    User authUser = (User) principal;
                    recordUser(request.getSession().getId(), true);
                    return true; // 说明已经登录了
                } else {
                    // 验证 token
                    String signToken = null;
                    // from cookie
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null)
                        for (Cookie cookie : cookies) {
                            if ("token".equals(cookie.getName())) {
                                signToken = cookie.getValue();
                                if (signToken.trim().length() == 0) {
                                    continue;
                                }
                                break;
                            }
                        }
                    if (signToken != null) {
                        // 验证 token 是否有效
                        PhoneCodeToken phoneCodeToken = jwtUtil.parseToken(signToken);
                        if (phoneCodeToken != null && phoneCodeToken.getCode() != null) {
                            // 更新登录信息
                            appAuthFlushService.flush(Long.parseLong(phoneCodeToken.getCode()));
                            recordUser(request.getSession().getId(), true);
                        }
                        return true;
                    }
                    recordUser(request.getSession().getId(), false);
                }
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                response.setCharacterEncoding("UTF-8");
//                response.setHeader("Content-type", "application/json;charset=UTF-8");
                // 给页面添加是否登录状态信息
//                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                if (principal != null && !principal.equals("anonymousUser") && modelAndView != null) {
//                    User authUser = (User) principal;
//                    modelAndView.addObject("signed", authUser.getUsername()); // email
//                }
            }
        }).addPathPatterns("/api/**").excludePathPatterns("/signin", "/signin/**", "/*.xml", "/export/**");
    }
}
