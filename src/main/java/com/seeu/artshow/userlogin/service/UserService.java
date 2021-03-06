package com.seeu.artshow.userlogin.service;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User findOne(Long uid) throws NoSuchUserException;

    User findByPhone(String phone) throws NoSuchUserException;

    User findByNickName(String nickname) throws NoSuchUserException;

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

    void delete(Long uid) throws NoSuchUserException;

    // admin

    List<User> findAll(User.TYPE type);

    Page<UserVO> findAll(String word, Pageable pageable);

    Page<UserVO> findAll(User.TYPE type, String word, Pageable pageable);

    boolean isAdminX(Long uid) throws NoSuchUserException;

    boolean isAdmin(Long uid) throws NoSuchUserException;
}
