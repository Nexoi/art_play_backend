package com.seeu.configurer.auth;

import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.utils.Util4IP;
import com.seeu.artshow.userlogin.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by neo on 10/10/2017.
 * <p>
 * 登录成功后返回登录前页面
 */
@Component("loginSuccessHandle")
public class LoginSuccessHandle implements AuthenticationSuccessHandler {

    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private Util4IP util4IP;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // TODO
//        Object url = httpServletRequest.getSession().getAttribute("loginSuccessURL");
//        String redirectUrl = url == null ? "/" : url.toString();
//        if (redirectUrl.contains("/signin")) {
//            redirectUrl = "/";
//        }
//        httpServletResponse.sendRedirect(redirectUrl);
        // 更新登陆信息
        if (!authentication.isAuthenticated())
            return;
        Long uid = ((User) authentication.getPrincipal()).getUid();
        String ip = util4IP.getIpAddress(httpServletRequest);
        userLoginLogService.updateLog(uid, ip, new Date());
    }
}
