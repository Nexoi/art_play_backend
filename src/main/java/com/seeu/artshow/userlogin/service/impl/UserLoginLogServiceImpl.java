package com.seeu.artshow.userlogin.service.impl;

import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.repository.UserRepository;
import com.seeu.artshow.userlogin.service.UserLoginLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
    @Resource
    private UserRepository userRepository;

    @Override
    public void updateLog(Long uid, String ip, Date time) {
        User ul = userRepository.findOne(uid);
        if (uid == null) return;
        ul.setLastLoginIp(ip);
        ul.setLastLoginTime(time);
        userRepository.save(ul);
    }
}
