package com.seeu.configurer.auth;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.ThirdUserLogin;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.ThirdPartTokenService;
import com.seeu.artshow.userlogin.service.ThirdUserLoginService;
import com.seeu.artshow.userlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by neo on 08/10/2017.
 */
@Component("seeuAuthenticationProvider")
public class YWQAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;
    @Autowired
    private ThirdUserLoginService thirdUserLoginService;
    @Autowired
    private ThirdPartTokenService thirdPartTokenService;

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


        // third part login
        // password 就是 token
        ThirdUserLogin tul = thirdUserLoginService.findByName(name);
        if (tul != null
                && tul.getArtUid() != null) {
            // 验证第三方
            final Map<String, String> map = new HashMap();
            thirdPartTokenService.validatedInfo(tul.getType(), name, password, (isValidated, username, nickname, headIconUrl) -> {
                if (isValidated) {
                    map.put("ok", "ok");
                    map.put("nickname", nickname);
                    map.put("headIconUrl", headIconUrl);
                } else {
                    map.put("ok", "notok");
                    map.put("nickname", null);
                    map.put("headIconUrl", null);
                }
            });
            // start find uid
            if ("ok".equals(map.get("ok"))) {
                User ul = null;
                try {
                    ul = userService.findOne(tul.getArtUid());
                    if (ul.getMemberStatus() != null
                            && ul.getMemberStatus() != User.USER_STATUS.UNACTIVED
                            && ul.getMemberStatus() != User.USER_STATUS.DISTORY) {
                        return new UsernamePasswordAuthenticationToken(ul, password, ul.getAuthorities());
                    }
                } catch (NoSuchUserException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
