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
import java.util.ArrayList;
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
    public List<Show> listAllShowForAdmin(Long uid) {
        List<ShowAuth> showAuths = repository.findAllByUid(uid);
        if (showAuths.isEmpty()) return new ArrayList<>();
        List<Long> showIds = showAuths.parallelStream().map(ShowAuth::getShowId).collect(Collectors.toList());
        List<Show> shows = showService.findAll(showIds);
        for (Show show : shows) {
            show.setMaps(null);
            show.setShowHallNames(null);
        }
        return shows;
    }

    @Override
    public void addShowAuthForAdmin(Long uid, Long showId) throws ResourceNotFoundException, NoSuchUserException {
        User user = userService.findOne(uid);
        Show show = showService.findOne(showId);
        ShowAuth auth = new ShowAuth();
        auth.setShowId(show.getId());
        auth.setUid(user.getUid());
        auth.setUpdateTime(new Date());
        repository.save(auth);
    }

    @Override
    public void deleteShowAuth(Long uid, Long showId) {
        ShowAuth auth = new ShowAuth();
        auth.setShowId(showId);
        auth.setUid(uid);
        auth.setUpdateTime(new Date());
        repository.delete(auth);
    }
}
