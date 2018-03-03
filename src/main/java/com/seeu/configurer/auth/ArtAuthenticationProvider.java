package com.seeu.configurer.auth;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


/**
 * Created by neo on 08/10/2017.
 */
@Component("seeuAuthenticationProvider")
public class ArtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 普通 账号／密码 验证
        // 用户必须为【正常状态】用户
        User user = null;
        try {
            user = userService.findByPhone(name);
            if (user.getPassword().equals(password)
                    && user.getMemberStatus() != null
                    && user.getMemberStatus() != User.USER_STATUS.UNACTIVED
                    && user.getMemberStatus() != User.USER_STATUS.DISTORY) {
                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            }
        } catch (NoSuchUserException e) {
            return null;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
