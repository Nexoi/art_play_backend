package com.seeu.artshow.utils.impl;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserService;
import com.seeu.artshow.utils.AppAuthFlushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppAuthFlushServiceImpl implements AppAuthFlushService {

    @Autowired
    private UserService userService;

    @Override
    public void flush(Long uid) {
        try {
            User ul = userService.findOne(uid);
            // flush
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication auth = new UsernamePasswordAuthenticationToken(ul, ul.getPassword(), ul.getAuthorities());
            context.setAuthentication(auth); //重新设置上下文中存储的用户权限
        } catch (NoSuchUserException e) {
            return;
        }
    }
}
