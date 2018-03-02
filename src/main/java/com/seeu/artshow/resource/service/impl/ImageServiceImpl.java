package com.seeu.artshow.resource.service.impl;

import com.seeu.artshow.resource.model.Image;
import com.seeu.artshow.resource.repository.ImageRepository;
import com.seeu.artshow.resource.service.ImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    private ImageRepository imageRepository;

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }
}
