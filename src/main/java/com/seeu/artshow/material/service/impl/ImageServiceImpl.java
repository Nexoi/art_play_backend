package com.seeu.artshow.material.service.impl;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.repository.ImageRepository;
import com.seeu.artshow.material.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

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

    @Override
    public Image changeName(Long imageId, String name) throws ResourceNotFoundException {
        Image image = findOne(imageId);
        image.setName(name);
        return save(image);
    }

    @Override
    public void delete(Long imageId) {
        repository.delete(imageId);
    }

    @Override
    public void delete(Collection<Long> imageIds) {
        List<Image> imageList = repository.findAll(imageIds);
        if (imageList.isEmpty()) return;
        repository.delete(imageList);
    }
}
