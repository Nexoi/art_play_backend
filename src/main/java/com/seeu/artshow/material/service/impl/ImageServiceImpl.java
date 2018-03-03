package com.seeu.artshow.material.service.impl;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.repository.ImageRepository;
import com.seeu.artshow.material.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    private ImageRepository repository;

    @Override
    public Page<Image> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Image> findAll(Long folderId, Pageable pageable) {
        return repository.findAllByFolderId(folderId, pageable);
    }

    @Override
    public Image findOne(Long id) throws ResourceNotFoundException {
        Image image = repository.findOne(id);
        if (image == null)
            throw new ResourceNotFoundException("image", "id: " + id);
        return image;
    }

    @Override
    public Image save(Image image) {
        return repository.save(image);
    }
}
