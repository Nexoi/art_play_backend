package com.seeu.artshow.userlogin.service.impl;

import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.repository.UserRepository;
import com.seeu.artshow.userlogin.service.UserService;
import com.seeu.artshow.userlogin.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    public User findByNickName(String nickname) throws NoSuchUserException {
        User user = repository.findByNickname(nickname);
        if (user == null)
            throw new NoSuchUserException(null, "[NickName]" + nickname);
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

    @Override
    public void delete(Long uid) throws NoSuchUserException {
        User user = findOne(uid);
        repository.delete(user);
        // TODO 删除用户
    }

    @Override
    public Page<UserVO> findAll(String word, Pageable pageable) {
        Page page = null;
        if (word == null || word.length() == 0)
            page = repository.findAll(pageable);
        else {
            String w = "%" + word + "%";
            page = repository.findAllByNicknameLikeOrPhoneLike(w, w, pageable);
        }
        if (page != null && page.getContent().size() != 0) {
            List<UserVO> vos = new ArrayList<>();
            List<User> users = page.getContent();
            for (User user : users) {
                if (user == null) continue;
                UserVO vo = new UserVO();
                BeanUtils.copyProperties(user, vo);
                vos.add(vo);
            }
            return new PageImpl<UserVO>(vos, pageable, page.getTotalElements());
        }
        return new PageImpl<UserVO>(new ArrayList<>());
    }

    @Override
    public Page<UserVO> findAll(User.TYPE type, String word, Pageable pageable) {
        Page page = null;
        if (word == null || word.length() == 0)
            page = repository.findAllByType(type, pageable);
        else {
            String w = "%" + word + "%";
            page = repository.findAllByTypeAndNicknameLikeOrPhoneLike(type, w, w, pageable);
        }
        if (page != null && page.getContent().size() != 0) {
            List<UserVO> vos = new ArrayList<>();
            List<User> users = page.getContent();
            for (User user : users) {
                if (user == null) continue;
                UserVO vo = new UserVO();
                BeanUtils.copyProperties(user, vo);
                vos.add(vo);
            }
            return new PageImpl<UserVO>(vos, pageable, page.getTotalElements());
        }
        return new PageImpl<UserVO>(new ArrayList<>());
    }
}
