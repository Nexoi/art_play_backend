package com.seeu.artshow.show.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.artshow.show.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.repository.ResourceGroupRepository;
import com.seeu.artshow.show.service.BeaconService;
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

    @Override
    public Page<ResourceGroup> findAllFilterSwitch(Long showId, Pageable pageable) {
        // TODO 无法过滤
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByShowId(showId, request);
    }

    @Override
    public List<ResourceGroup> findAllFilterSwitch(Long showId) {
        // TODO 已经过滤
        List<ResourceGroup> groups = repository.findAllByShowId(showId);
        List<ResourceGroup> groupList = groups.parallelStream().filter(it -> {
            List<Beacon> beaconList = it.getBeacons();
            if (beaconList.isEmpty()) return true;
            for (Beacon beacon : beaconList) {
                boolean flag = (null != beacon.getStatus() && Beacon.STATUS.on == beacon.getStatus());
                if (flag) return true;
            }
            return false;
        }).collect(Collectors.toList());
        return groupList;
    }

    @Override
    public List<ResourceGroup> findAllByBeaconFilterSwitch(Long showId) {
        List<ResourceGroup> groups = repository.findAllByShowIdAndBeaconsNotNull(showId);
        List<ResourceGroup> groupList = groups.parallelStream().filter(it -> {
            List<Beacon> beaconList = it.getBeacons();
            if (beaconList.isEmpty()) return false;
            for (Beacon beacon : beaconList) {
                boolean flag = (null != beacon.getStatus() && Beacon.STATUS.on == beacon.getStatus());
                if (flag) return true;
            }
            return false;
        }).collect(Collectors.toList());
        return groupList;
    }

    @Override
    public Page<ResourceGroup> findAllByBeaconFilterSwitch(Long showId, Pageable pageable) {
        // TODO 无法过滤
        // 查询参数
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByShowIdAndBeaconsNotNull(showId, request);
    }

    @Override
    public Page<ResourceGroup> findAllFilterSwitch(Long showId, Long mapId, Pageable pageable) throws ResourceNotFoundException {
        // TODO 无法过滤
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Beacon> page = beaconService.findAll(showId, mapId, request);
        List<Beacon> beaconList = page.getContent();
        if (beaconList.isEmpty()) return new PageImpl<ResourceGroup>(new ArrayList<>());
        List<Long> showIds = beaconList.parallelStream().map(Beacon::getShowId).collect(Collectors.toList());
        List<ResourceGroup> groups = repository.findAllByShowIdIn(showIds);
        return new PageImpl<ResourceGroup>(groups, request, page.getTotalElements());
    }

    @Override
    public List<ResourceGroup> findAllFilterSwitch(Long showId, Long mapId) throws ResourceNotFoundException {
        List<Beacon> beacons = beaconService.findAll(showId, mapId);
        if (beacons.isEmpty()) return new ArrayList<>();
        List<Long> showIds = beacons.parallelStream().map(Beacon::getShowId).collect(Collectors.toList());
        List<ResourceGroup> groups = repository.findAllByShowIdIn(showIds);
        List<ResourceGroup> groupList = groups.parallelStream().filter(it -> {
            List<Beacon> beaconList = it.getBeacons();
            if (beaconList.isEmpty()) return false;
            for (Beacon beacon : beaconList) {
                boolean flag = (null != beacon.getStatus() && Beacon.STATUS.on == beacon.getStatus());
                if (flag) return true;
            }
            return false;
        }).collect(Collectors.toList());
        return groupList;
    }

    @Override
    public ResourceGroup findOneFilterSwitch(Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = repository.findOne(groupId);
        if (group == null) throw new ResourceNotFoundException("resources_group", "id: " + groupId);
        List<Beacon> beaconList = group.getBeacons();
        boolean flag = false;
        if (!beaconList.isEmpty())
            for (Beacon beacon : beaconList) {
                flag = (null != beacon.getStatus() && Beacon.STATUS.on == beacon.getStatus());
                if (flag) break;
            }
        if (flag) return group;
        throw new ResourceNotFoundException("resources_group", "id: " + groupId);
    }

    @Override
    public List<ResourceGroup> findAllByBeaconUUIDFilterSwitch(Long showId, String uuid) throws ResourceNotFoundException, ActionParameterException {
        List<Beacon> beacons = beaconService.findOne(showId, uuid);
        if (beacons.isEmpty()) throw new ActionParameterException("该 beacon 未绑定任何资源组信息");
        List<Long> showIds = beacons.parallelStream().filter(it -> (null != it.getStatus() && Beacon.STATUS.on == it.getStatus())).map(Beacon::getShowId).collect(Collectors.toList());
        List<ResourceGroup> groups = repository.findAllByShowIdIn(showIds);
        return groups;
    }

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
    public List<ResourceGroup> findAll(Collection<Long> groupIds) {
        if (null == groupIds || groupIds.isEmpty()) return new ArrayList<>();
        return repository.findAll(groupIds);
    }

    @Override
    public List<ResourceGroup> findAllByShowId(Collection<Long> showIds) {
        if (null == showIds || showIds.isEmpty()) return new ArrayList<>();
        return repository.findAllByShowIdIn(showIds);
    }

    // 根据 mapId 找的 必须要有 beacon 信息
    @Override
    public Page<ResourceGroup> findAll(Long showId, Long mapId, Pageable pageable) throws ResourceNotFoundException {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Beacon> page = beaconService.findAll(showId, mapId, request);
        List<Beacon> beaconList = page.getContent();
        if (beaconList.isEmpty()) return new PageImpl<ResourceGroup>(new ArrayList<>());
        List<Long> showIds = beaconList.parallelStream().map(Beacon::getShowId).collect(Collectors.toList());
        List<ResourceGroup> groups = repository.findAllByShowIdIn(showIds);
        return new PageImpl<ResourceGroup>(groups, request, page.getTotalElements());
    }

    // 根据 mapId 找的 必须要有 beacon 信息
    @Override
    public List<ResourceGroup> findAll(Long showId, Long mapId) throws ResourceNotFoundException {
        List<Beacon> beaconList = beaconService.findAll(showId, mapId);
        if (beaconList.isEmpty()) return new ArrayList<>();
        List<Long> showIds = beaconList.parallelStream().map(Beacon::getShowId).collect(Collectors.toList());
        return repository.findAllByShowIdIn(showIds);
    }

    @Override
    public Page<ResourceGroup> searchAll(Long showId, String name, Pageable pageable) {
        return repository.findAllByNameLikeOrderByUpdateTimeDesc("%" + name + "%", pageable);
    }

    @Override
    public List<ResourceGroup> findAllByBeacon(Long showId) {
        return repository.findAllByShowIdAndBeaconsNotNull(showId);
    }

    @Override
    public Page<ResourceGroup> findAllByBeacon(Long showId, Pageable pageable) {
        // 查询参数
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByShowIdAndBeaconsNotNull(showId, request);
    }

    @Override
    public Page<ResourceGroup> findAllByAR(Long showId, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "arBindTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByShowIdAndArNotNull(showId, request);
    }

    @Override
    public ResourceGroup findOne(Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = repository.findOne(groupId);
        if (group == null) throw new ResourceNotFoundException("resources_group", "id: " + groupId);
        return group;
    }

    // No Usages
    @Override
    public List<ResourceGroup> findAllByBeaconUUID(Long showId, String uuid) throws ResourceNotFoundException, ActionParameterException {
        List<Beacon> beacons = beaconService.findOne(showId, uuid);
        if (beacons.isEmpty()) throw new ActionParameterException("该 beacon 未绑定任何资源组信息");
        List<Long> showIds = beacons.parallelStream().map(Beacon::getShowId).collect(Collectors.toList());
        List<ResourceGroup> groups = repository.findAllByShowIdIn(showIds);
//        return findOne(beacon.getResourceGroup().getId());
        return groups;
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
//        group.setPositionHeight(0);
//        group.setPositionWidth(0);
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
    public ResourceGroup bindBeacons(Long showId, Long groupId, Collection<String> uuids) throws ResourceNotFoundException, ActionParameterException {
        if (uuids == null || uuids.isEmpty())
            throw new ActionParameterException("传入参数 [beacon:uuids] 不能为空");
        ResourceGroup group = findOne(groupId);
        List<Beacon> beacons = beaconService.findAll(showId, uuids); // TODO
        if (beacons == null || beacons.isEmpty())
            throw new ActionParameterException("传入参数 [beacon:uuids] 有误，无此 beacon 信息");
        group.setBeacons(beacons);
        group.setBeaconsBindTime(new Date());
        return repository.save(group);
        // XXXX 将第一个 Beacon 的信息作为 group 的地理信息
//        Beacon beacon = beacons.get(0);
//        group = bindMapInfo(groupId, beacon.getPositionWidth(), beacon.getPositionHeight(), beacon.getShowMap());
//        return group;
    }

    // 清空绑定
    @Override
    public ResourceGroup cleanBeacons(Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = findOne(groupId);
        group.setBeacons(new ArrayList<>());
        group.setBeaconsBindTime(null);
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
    public ResourceGroup cancelBindAR(Long groupId) throws ResourceNotFoundException {
        ResourceGroup group = findOne(groupId);
        group.setArBindTime(null);
        group.setAr(null);
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
    public void deleteAllByShowId(Long showId) {
        if (null == showId) return;
        repository.delByShowId(showId);
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
