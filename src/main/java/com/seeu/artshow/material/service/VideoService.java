package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoService {

    Page<Video> findAll(Pageable pageable);

    Page<Video> findAll(Long folderId, Pageable pageable);

    Video findOne(Long id) throws ResourceNotFoundException;

    
    Video save(Video video);
}
