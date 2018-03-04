package com.seeu.artshow.show.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.repository.ShowRepository;
import com.seeu.artshow.show.service.ShowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {
    @Resource
    private ShowRepository repository;

    @Override
    public Show findOne(Long showId) throws ResourceNotFoundException {
        Show show = repository.findOne(showId);
        if (show == null) throw new ResourceNotFoundException("show", "id: " + showId);
        return show;
    }

    @Override
    public void viewOnce(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
        repository.viewItOnce(show.getId());
    }

    @Override
    public void likeOnce(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
        repository.likeItOnce(show.getId());
    }

    @Override
    public void cancelLikeOnce(Long showId) throws ResourceNotFoundException {
        Show show = findOne(showId);
        repository.cancelLikeItOnce(show.getId());
    }

    @Override
    public Page<Show> findAll(Pageable pageable) {
        return repository.findAllByOrderByStartTimeDesc(pageable);
    }

    @Override
    public Page<Show> searchAll(String title, Pageable pageable) {
        return repository.findAllByTitleLikeOrderByStartTimeDesc("%" + title + "%", pageable);
    }

    @Override
    public List<Show> findAll(Collection<Long> showIds) {
        if (showIds == null || showIds.isEmpty()) return new ArrayList<>();
        return repository.findAllByIdIn(showIds);
    }

    @Override
    public Show add(Show show) throws ActionParameterException {
        if (show == null) throw new ActionParameterException("参数不能为空");
        show.setId(null);
        show.setLikeTimes(0L);
        show.setViewTimes(0L);
        show.setUpdateTime(new Date());
        return repository.save(show);
    }

    @Override
    public Show update(Show show) throws ActionParameterException, ResourceNotFoundException {
        if (show == null || show.getId() == null) throw new ActionParameterException("参数不能为空");
        Show savedShow = findOne(show.getId());
        if (show.getStartTime() != null) savedShow.setStartTime(show.getStartTime());
        if (show.getEndTime() != null) savedShow.setEndTime(show.getEndTime());
        if (show.getTitle() != null) savedShow.setTitle(show.getTitle());
        if (show.getIntroduceText() != null) savedShow.setIntroduceText(show.getIntroduceText());
        if (show.getShowHallName() != null) savedShow.setShowHallName(show.getShowHallName());
        if (show.getPosterImage() != null) savedShow.setPosterImage(show.getPosterImage());
        return repository.save(savedShow);
    }

    @Override
    public void delete(Long showId) {
        repository.delete(showId);
    }
}
