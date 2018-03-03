package com.seeu.artshow.userlogin.service;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;

public interface UserService {
    User findOne(Long uid) throws NoSuchUserException;

    User findByPhone(String phone) throws NoSuchUserException;

    User findByThirdPartUserName(String username) throws NoSuchUserException;

    User add(User user);

    User save(User user);

    /**
     * 判断空值
     *
     * @param user
     * @return
     */
    User insertSelective(User user) throws NoSuchUserException;

}
