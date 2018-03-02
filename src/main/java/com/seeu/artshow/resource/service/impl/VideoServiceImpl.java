package com.seeu.artshow.resource.service.impl;

import com.seeu.artshow.resource.model.Video;
import com.seeu.artshow.resource.repository.VideoRepository;
import com.seeu.artshow.resource.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoRepository repository;

    @Override
    public Video save(Video video) {
        return repository.save(video);
    }
}
