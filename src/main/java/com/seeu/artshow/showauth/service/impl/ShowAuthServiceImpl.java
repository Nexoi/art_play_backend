package com.seeu.artshow.showauth.service.impl;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.artshow.showauth.model.ShowAuth;
import com.seeu.artshow.showauth.repository.ShowAuthRepository;
import com.seeu.artshow.showauth.service.ShowAuthService;
import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.userlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowAuthServiceImpl implements ShowAuthService {
    @Resource
    private ShowAuthRepository repository;
    @Autowired
    private ShowService showService;
    @Autowired
    private UserService userService;

    @Override
    public List<Show> listAllShowForAdmin(Long uid) throws NoSuchUserException {
        List<Show> shows = null;
        if (userService.isAdminX(uid)) {
            shows = showService.findAll();
        } else {
            List<ShowAuth> showAuths = repository.findAllByUid(uid);
            if (showAuths.isEmpty()) return new ArrayList<>();
            List<Long> showIds = showAuths.parallelStream().map(ShowAuth::getShowId).collect(Collectors.toList());
            shows = showService.findAll(showIds);
        }
        if (null == shows || shows.isEmpty())
            return new ArrayList<>();
        for (Show show : shows) {
            show.setMaps(null);
            show.setShowHallNames(null);
        }
        return shows;
    }

    @Transactional
    @Override
    public void updateShowAuthForAdmin(Long uid, Collection<Long> showIds) throws ResourceNotFoundException, NoSuchUserException {
        User user = userService.findOne(uid);
        List<Show> shows = showService.findAll(showIds);
        if (shows.isEmpty())return;
        // delete all
        repository.deleteAllByUid(user.getUid());
        // add
        List<ShowAuth> auths = new ArrayList<>();
        for(Show show : shows){
            ShowAuth auth = new ShowAuth();
            auth.setShowId(show.getId());
            auth.setUid(user.getUid());
            auth.setUpdateTime(new Date());
            auths.add(auth);
        }
        repository.save(auths);
    }

    // 作废
    @Deprecated
    @Override
    public void deleteShowAuth(Long uid, Long showId) {
        ShowAuth auth = new ShowAuth();
        auth.setShowId(showId);
        auth.setUid(uid);
        auth.setUpdateTime(new Date());
        repository.delete(auth);
    }
}
