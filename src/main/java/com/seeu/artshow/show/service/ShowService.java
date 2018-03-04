package com.seeu.artshow.show.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Show;
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

    Page<Show> findAll(Pageable pageable);

    Page<Show> searchAll(String title, Pageable pageable);

    List<Show> findAll(Collection<Long> showIds);

    Show add(@Validated Show show) throws ActionParameterException;

    Show update(@Validated Show show) throws ActionParameterException, ResourceNotFoundException;

    void delete(Long showId);

}
