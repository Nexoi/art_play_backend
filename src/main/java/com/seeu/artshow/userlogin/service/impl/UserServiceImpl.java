package com.seeu.artshow.userlogin.service.impl;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.repository.UserRepository;
import com.seeu.artshow.userlogin.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository repository;

    @Override
    public User findOne(Long uid) throws NoSuchUserException {
        User user = repository.findOne(uid);
        if (user == null)
            throw new NoSuchUserException(uid, null);
        return user;
    }

    @Override
    public User findByPhone(String phone) throws NoSuchUserException {
        User user = repository.findByPhone(phone);
        if (user == null)
            throw new NoSuchUserException(null, phone);
        return user;
    }

    @Override
    public User findByThirdPartUserName(String username) throws NoSuchUserException {
        User user = repository.findByThirdPartName(username);
        if (user == null)
            throw new NoSuchUserException(null, username);
        return user;
    }

    @Override
    public User add(User user) {
        if (user == null) return null;
        user.setUid(null);
        return repository.save(user);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User insertSelective(User user) throws NoSuchUserException {
        if (user == null) return null;
        if (user.getUid() == null) throw new NoSuchUserException(null, null);
        User existedUser = findOne(user.getUid());
        // 判断空
        if (user.getHeadIconUrl() != null) existedUser.setHeadIconUrl(user.getHeadIconUrl());
        if (user.getNickname() != null) existedUser.setNickname(user.getNickname());
        // if (user.getPhone() != null) existedUser.setPhone(user.getPhone()); // 不允许修改手机！
        if (user.getGender() != null) existedUser.setGender(user.getGender());
        return repository.save(existedUser);
    }
}
