package com.seeu.artshow.showauth.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.userlogin.exception.NoSuchUserException;

import java.util.List;

public interface ShowAuthService {

    List<Show> listAllShowForAdmin(Long uid) throws NoSuchUserException; // 这个 Show 只包含 showId, title 信息

    void addShowAuthForAdmin(Long uid, Long showId) throws ResourceNotFoundException, NoSuchUserException;

    void deleteShowAuth(Long uid, Long showId);


}
