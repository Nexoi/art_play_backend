package com.seeu.artshow.material.service.impl;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Audio;
import com.seeu.artshow.material.repository.AudioRepository;
import com.seeu.artshow.material.service.AudioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AudioServiceImpl implements AudioService {

    @Resource
    private AudioRepository repository;

    @Override
    public Page<Audio> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Audio> findAll(Long folderId, Pageable pageable) {
        return repository.findAllByFolderId(folderId, pageable);
    }

    @Override
    public Audio findOne(Long id) throws ResourceNotFoundException {
        Audio audio = repository.findOne(id);
        if (audio == null)
            throw new ResourceNotFoundException("audio", "id: " + id);
        return audio;
    }

    @Override
    public Audio save(Audio audio) {
        return repository.save(audio);
    }
}
