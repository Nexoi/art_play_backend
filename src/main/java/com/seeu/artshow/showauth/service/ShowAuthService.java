package com.seeu.artshow.showauth.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.userlogin.exception.NoSuchUserException;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface ShowAuthService {

    List<Show> listAllShowForAdmin(Long uid) throws NoSuchUserException; // 这个 Show 只包含 showId, title 信息

    List<Long> listAllShowIdForAdmin(Long uid);

    // 会清空之前的所有数据再更新
    @Transactional
    void updateShowAuthForAdmin(Long uid, Collection<Long> showIds) throws ResourceNotFoundException, NoSuchUserException;

    @Transactional
    void deleteAllShowAuth(Long uid) throws NoSuchUserException;


}
