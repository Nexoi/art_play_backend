package com.seeu.artshow.installation.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.installation.repository.ShowMapRepository;
import com.seeu.artshow.installation.service.ShowMapService;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ShowMapServiceImpl implements ShowMapService {
    @Resource
    private ShowMapRepository repository;
    @Autowired
    private ImageService imageService;

    @Override
    public ShowMap findOne(Long mapId) throws ResourceNotFoundException {
        ShowMap map = repository.findOne(mapId);
        if (map == null) throw new ResourceNotFoundException("无此地图 show_map", "id: " + mapId);
        return map;
    }

    @Override
    public Page<ShowMap> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ShowMap> findAll(Long showId, Pageable pageable) {
        return repository.findAllByShowId(showId, pageable);
    }

    @Override
    public ShowMap add(ShowMap map) throws ActionParameterException {
        if (map == null) throw new ActionParameterException("参数传入不完整【ShowMap】");
        map.setId(null);
        map.setUpdateTime(new Date());
        return repository.save(map);
    }

    @Override
    public ShowMap update(ShowMap map) throws ResourceNotFoundException, ActionParameterException {
        if (map == null || map.getId() == null) throw new ActionParameterException("参数传入不完整【ShowMap】");
        ShowMap savedMap = findOne(map.getId());
        // showId 不可修改
        if (map.getFloor() != null) savedMap.setFloor(map.getFloor());
        if (map.getHeight() != null) savedMap.setHeight(map.getHeight());
        if (map.getWidth() != null) savedMap.setWidth(map.getWidth());
        if (map.getImage() != null) savedMap.setImage(map.getImage());
        if (map.getName() != null) savedMap.setName(map.getName());
        if (map.getShowHallName() != null) savedMap.setShowHallName(map.getShowHallName());
        savedMap.setUpdateTime(new Date());
        return repository.save(savedMap);
    }

    @Override
    public ShowMap updateMap(Long mapId, Long imageId) throws ActionParameterException, ResourceNotFoundException {
        if (mapId == null) throw new ActionParameterException("参数传入不完整【ShowMap:id】");
        ShowMap savedMap = findOne(mapId);
        Image image = imageService.findOne(imageId);
        savedMap.setImage(image);
        return repository.save(savedMap);
    }

    @Override
    public void delete(Long mapId) {
        repository.delete(mapId);
    }
}
