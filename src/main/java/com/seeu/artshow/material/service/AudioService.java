package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Audio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface AudioService {

    Page<Audio> findAll(Pageable pageable);

    Page<Audio> findAll(Long folderId, Pageable pageable);

    Audio findOne(Long id) throws ResourceNotFoundException;

    Audio save(Audio audio);

    Audio changeName(Long audioId, String name) throws ResourceNotFoundException;

    void delete(Long audioId);

    void delete(Collection<Long> audioIds);
}
