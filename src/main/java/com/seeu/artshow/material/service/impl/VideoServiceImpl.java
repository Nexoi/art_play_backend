package com.seeu.artshow.material.service.impl;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Video;
import com.seeu.artshow.material.repository.VideoRepository;
import com.seeu.artshow.material.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoRepository repository;

    @Override
    public Page<Video> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Video> findAll(Long folderId, Pageable pageable) {
        return repository.findAllByFolderId(folderId, pageable);
    }

    @Override
    public Video findOne(Long id) throws ResourceNotFoundException {
        Video video = repository.findOne(id);
        if (video == null) throw new ResourceNotFoundException("video", "id: " + id);
        return video;
    }

    @Override
    public Video save(Video video) {
        return repository.save(video);
    }
}
