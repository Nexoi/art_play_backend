package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface VideoService {

    Page<Video> findAll(Pageable pageable);

    Page<Video> findAll(Long folderId, Pageable pageable);

    Video findOne(Long id) throws ResourceNotFoundException;

    Video save(Video video);

    Video changeName(Long videoId, String name) throws ResourceNotFoundException;

    void delete(Long videoId);

    void delete(Collection<Long> videoIds);
}
