package com.seeu.artshow.show.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.userlogin.exception.NoSuchUserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

public interface ShowService {
    Show findOne(Long showId) throws ResourceNotFoundException;

    void viewOnce(Long showId) throws ResourceNotFoundException;

    void likeOnce(Long showId) throws ResourceNotFoundException;

    void cancelLikeOnce(Long showId) throws ResourceNotFoundException;

    void audioPlusOne(Long showId) throws ResourceNotFoundException;

    void audioMinusOne(Long showId) throws ResourceNotFoundException;

    Page<Show> findAll(Pageable pageable);

    Page<Show> searchAll(String title, Pageable pageable);

    Page<Show> findAll(Long adminUid, Pageable pageable) throws NoSuchUserException;

    Page<Show> searchAll(Long adminUid, String title, Pageable pageable) throws NoSuchUserException;

    List<Show> findAll(Collection<Long> showIds);

    List<Show> findAll(); // 以后会加上管理员 id 判断来过滤资源

    Show add(@Validated Show show, Image image) throws ActionParameterException;

    Show update(@Validated Show show, Image image) throws ActionParameterException, ResourceNotFoundException;

    void delete(Long showId) throws ResourceNotFoundException;

}
