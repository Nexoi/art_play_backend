package com.seeu.artshow.show.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.Beacon;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.installation.service.BeaconService;
import com.seeu.artshow.installation.service.ShowMapService;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.repository.ResourceGroupRepository;
import com.seeu.artshow.show.service.ResourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceGroupServiceImpl implements ResourceGroupService {
    @Resource
    private ResourceGroupRepository repository;
    @Autowired
    private BeaconService beaconService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ShowMapService showMapService;

    @Override
    public Page<ResourceGroup> findAll(Long showId, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByShowId(showId, request);
    }

    @Override
    public List<ResourceGroup> findAll(Long showId) {
        return repository.findAllByShowId(showId);
    }

    @Override
    public Page<ResourceGroup> findAll(Long showId, Long mapId, Pageable pageable) throws ResourceNotFoundException {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        ShowMap map = showMapService.findOne(mapId);
        return repository.findAllByShowMap(map, request);
    }

    @Override
    public List<ResourceGroup> findAll(Long showId, Long mapId) throws ResourceNotFoundException {
        ShowMap map = showMapService.findOne(mapId);
        return repository.findAllByShowMap(map);
    }

    @Override
    public Page<ResourceGroup> searchAll(Long showId, String name, Pageable pageable) {
        return repository.findAllByNameLikeOrderByUpdateTimeDesc("%" + name + "%", pageable);
    }

    @Override
    public Page<Beacon> findAllByBeacon(Long showId, Pageable pageable) {
        List<ResourceGroup> showFolders = repository.findAllByShowId(showId);
        if (showFolders.isEmpty()) return new PageImpl<Beacon>(new ArrayList<>());
        // 提取 ids
        List<Long> showFolderIds = showFolders.parallelStream().map(ResourceGroup::getId).collect(Collectors.toList());
        // 查询参数
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return beaconService.findAllByResourceGroupIds(showFolderIds, request);
    }

    @Override
    public Page<ResourceGroup> findAllByAR(Long showId, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "arBindTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByShowId(showId, request);
    }

    @Override
    public ResourceGroup findOne(Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = repository.findOne(groupId);
        if (group == null) throw new ResourceNotFoundException("resources_group", "id: " + groupId);
        return group;
    }

    @Override
    public ResourceGroup findOneByBeaconUUID(String uuid) throws ResourceNotFoundException, ActionParameterException {
        Beacon beacon = beaconService.findOne(uuid);
        if (null == beacon.getResourcesGroupId()) throw new ActionParameterException("该 beacon 未绑定任何资源组信息");
        return findOne(beacon.getResourcesGroupId());
    }

    @Override
    public ResourceGroup add(ResourceGroup group) throws ActionParameterException {
        if (group == null) throw new ActionParameterException("传入参数不能为空");
        if (group.getShowId() == null) throw new ActionParameterException("参数 [showId] 不能为空");
        // 初始化信息
        group.setViewTimes(0L);
        group.setLikeTimes(0L);
        group.setAr(null);
        group.setArBindTime(null);
        group.setBeacons(new ArrayList<>());
        group.setBeaconsBindTime(null);
        group.setPositionHeight(0);
        group.setPositionWidth(0);
        group.setId(null);
        group.setUpdateTime(new Date());
        return repository.save(group);
    }

    @Override
    public ResourceGroup changeName(Long groupId, String name) throws ActionParameterException, ResourceNotFoundException {
        if (name == null) throw new ActionParameterException("传入参数 [name] 不能为空");
        ResourceGroup group = findOne(groupId);
        group.setName(name);
        group.setUpdateTime(new Date());
        return repository.save(group);
    }

    @Override
    public ResourceGroup bindBeacons(Long groupId, Collection<String> uuids) throws ResourceNotFoundException, ActionParameterException {
        if (uuids == null || uuids.isEmpty())
            throw new ActionParameterException("传入参数 [beacon:uuids] 不能为空");
        ResourceGroup group = findOne(groupId);
        List<Beacon> beacons = beaconService.findAll(uuids);
        if (beacons == null || beacons.isEmpty())
            throw new ActionParameterException("传入参数 [beacon:uuids] 有误，无此 beacon 信息");
        group.setBeacons(beacons);
        group.setBeaconsBindTime(new Date());
        return repository.save(group);
    }

    @Override
    public ResourceGroup bindAR(Long groupId, Long imageId) throws ResourceNotFoundException {
        Image arImage = imageService.findOne(imageId);
        ResourceGroup group = findOne(groupId);
        group.setAr(arImage);
        group.setArBindTime(new Date());
        return repository.save(group);
    }

    @Override
    public void delete(Long groupId) {
        repository.delete(groupId);
    }

    @Override
    public void delete(Collection<Long> groupIds) {
        if (groupIds != null && !groupIds.isEmpty())
            repository.deleteAllByIdIn(groupIds);
    }

    @Override
    public void viewOnce(Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = findOne(groupId);
        repository.viewItOnce(group.getId());
    }

    @Override
    public void likeOnce(Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = findOne(groupId);
        repository.likeItOnce(group.getId());
    }

    @Override
    public void cancelLikeOnce(Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = findOne(groupId);
        repository.cancelLikeItOnce(group.getId());
    }
}
