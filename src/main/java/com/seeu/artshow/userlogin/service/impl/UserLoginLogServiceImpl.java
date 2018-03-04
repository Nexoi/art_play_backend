package com.seeu.artshow.userlogin.service.impl;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserLoginLogService;
import com.seeu.artshow.userlogin.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
    @Resource
    private UserService userService;

    @Override
    public void updateLog(Long uid, String ip, Date time) {
        try {
            User ul = userService.findOne(uid);
            ul.setLastLoginIp(ip);
            ul.setLastLoginTime(time);
            userService.save(ul);
        } catch (NoSuchUserException e) {
            // do nothing
        }
    }
}
