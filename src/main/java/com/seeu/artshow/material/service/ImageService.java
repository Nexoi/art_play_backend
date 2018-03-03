package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ImageService {
    Page<Image> findAll(Pageable pageable);

    Page<Image> findAll(Long folderId, Pageable pageable);

    Image findOne(Long id) throws ResourceNotFoundException;

    Image save(Image image);

    Image changeName(Long imageId, String name) throws ResourceNotFoundException;

    void delete(Long imageId);

    void delete(Collection<Long> imageIds);
}
