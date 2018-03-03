package com.seeu.artshow.material.service.impl;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Video;
import com.seeu.artshow.material.repository.VideoRepository;
import com.seeu.artshow.material.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

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

    @Override
    public Video changeName(Long videoId, String name) throws ResourceNotFoundException {
        Video video = findOne(videoId);
        video.setName(name);
        return save(video);
    }

    @Override
    public void delete(Long videoId) {
        repository.delete(videoId);
    }

    @Override
    public void delete(Collection<Long> videoIds) {
        List<Video> videoList = repository.findAll(videoIds);
        if (videoList.isEmpty()) return;
        repository.delete(videoList);
    }
}
