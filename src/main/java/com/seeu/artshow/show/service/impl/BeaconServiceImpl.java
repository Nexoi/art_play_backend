package com.seeu.artshow.show.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.InstallBeacon;
import com.seeu.artshow.show.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.repository.BeaconRepository;
import com.seeu.artshow.show.service.BeaconService;
import com.seeu.artshow.show.service.ResourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BeaconServiceImpl implements BeaconService {
    @Resource
    private BeaconRepository repository;
    @Autowired
    private ResourceGroupService resourceGroupService;

    @Override
    public List<Beacon> findOne(Long showId, String uuid) throws ResourceNotFoundException {
        if (null == showId || null == uuid)
            throw new ResourceNotFoundException("beacon", "showId/uuid: null");
        List<Beacon> beacons = repository.findAllByShowIdAndBasicInfo_Uuid(showId, uuid);
        if (null == beacons) throw new ResourceNotFoundException("beacon", "showId: " + showId + "uuid: " + uuid);
        return beacons;
    }

    @Override
    public Beacon findOne(Long showId, Long beaconId) throws ResourceNotFoundException {
        if (null == showId || null == beaconId)
            throw new ResourceNotFoundException("beacon", "showId/uuid: null");
        Beacon beacon = repository.findByShowIdAndBasicInfo_Id(showId, beaconId);
        if (null == beacon)
            throw new ResourceNotFoundException("beacon", "showId: " + showId + "beaconId: " + beaconId);
        return beacon;
    }

    @Override
    public List<Beacon> findAll(Long showId, Collection<String> uuids) {
        return repository.findAllByShowIdAndBasicInfo_UuidIn(showId, uuids);
    }

    @Override
    public List<Beacon> findAllWithEmptyBeacons(Long showId) {
        List<Beacon> beaconList = repository.findAllByShowId(showId);
        if (beaconList.isEmpty()) return beaconList;
        // 填充 group 信息
        List<Long> groupIds = beaconList.parallelStream().map(Beacon::getResourcesGroupId).filter(Objects::nonNull).collect(Collectors.toList());
        List<ResourceGroup> resourceGroups = resourceGroupService.findAll(groupIds);
        Map<Long, ResourceGroup> resourceGroupMap = new HashMap<>();
        for (ResourceGroup group : resourceGroups) {
            resourceGroupMap.put(group.getId(), group);
        }
        // fill
        for (Beacon beacon : beaconList) {
            ResourceGroup group = resourceGroupMap.get(beacon.getResourcesGroupId());
            if (null == group) continue;
            group.setBeacons(null);
            beacon.setResourceGroup(group);
        }
        return beaconList;
    }

    @Override
    public Page<Beacon> findAllWithEmptyBeacons(Long showId, Pageable pageable) {
        Page<Beacon> beaconPage = repository.findAllByShowId(showId, pageable);
        List<Beacon> beaconList = beaconPage.getContent();
        if (beaconList.isEmpty()) return beaconPage;
        // 填充 group 信息
        List<Long> groupIds = beaconList.parallelStream().map(Beacon::getResourcesGroupId).filter(Objects::nonNull).collect(Collectors.toList());
        List<ResourceGroup> resourceGroups = resourceGroupService.findAll(groupIds);
        Map<Long, ResourceGroup> resourceGroupMap = new HashMap<>();
        for (ResourceGroup group : resourceGroups) {
            resourceGroupMap.put(group.getId(), group);
        }
        // fill
        for (Beacon beacon : beaconList) {
            ResourceGroup group = resourceGroupMap.get(beacon.getResourcesGroupId());
            if (null == group) continue;
            group.setBeacons(null);
            beacon.setResourceGroup(group);
        }
        return beaconPage;
    }

    @Override
    public Page<Beacon> findAllMustBeaconed(Long showId, Pageable pageable) {
        return repository.findAllByShowIdAndResourcesGroupIdNotNull(showId, pageable); //
//        List<Beacon> beaconList = beaconPage.getContent();
//        List<Long> groupIds = beaconList.parallelStream().map(Beacon::getResourcesGroupId).collect(Collectors.toList());
//        List<ResourceGroup> groups = resourceGroupService.findAll(groupIds);
//        return new PageImpl<ResourceGroup>(groups, pageable, beaconPage.getTotalElements());
    }

    @Override
    public List<Beacon> findAllMustBeaconed(Long showId) {
        return repository.findAllByShowIdAndResourcesGroupIdNotNull(showId); //
//        List<Long> groupIds = beaconList.parallelStream().map(Beacon::getResourcesGroupId).collect(Collectors.toList());
//        return resourceGroupService.findAll(groupIds);
    }

    @Override
    public List<Beacon> findAll(Long showId, Long showMapId) {
        return repository.findAllByShowIdAndShowMap_Id(showId, showMapId);
    }

    @Override
    public Page<Beacon> findAll(Long showId, Long showMapId, Pageable pageable) {
        return repository.findAllByShowIdAndShowMap_Id(showId, showMapId, pageable);
    }


    // 只能修改 admin 可操作的基本信息
    @Override
    public Beacon update(Long showId, Long id, Beacon beacon) throws ActionParameterException, ResourceNotFoundException {
        Beacon savedBeacon = findOne(showId, id);
        if (null != beacon.getAvailableRange()) savedBeacon.setAvailableRange(beacon.getAvailableRange());
        if (null != beacon.getName()) savedBeacon.setName(beacon.getName());
        if (null != beacon.getPositionHeight() && null != beacon.getPositionWidth()) {
            savedBeacon.setPositionHeight(beacon.getPositionHeight());
            savedBeacon.setPositionWidth(beacon.getPositionWidth());
        }
//        if (null != beacon.getResourcesGroupId()) savedBeacon.setResourcesGroupId(beacon.getResourcesGroupId());
        if (null != beacon.getShowMap()) savedBeacon.setShowMap(beacon.getShowMap());
        if (null != beacon.getStatus()) savedBeacon.setStatus(beacon.getStatus());
        savedBeacon.setUpdateTime(new Date());
        return repository.save(savedBeacon);
    }

    @Override
    public Beacon changeStatus(Long showId, Long id) throws ResourceNotFoundException {
        Beacon beacon = findOne(showId, id);
        Beacon.STATUS status = beacon.getStatus();
        beacon.setStatus(Beacon.STATUS.on == status ? Beacon.STATUS.off : Beacon.STATUS.on);
        return repository.save(beacon);
    }

    @Override
    public Beacon removeBindInfo(Long showId, Long id) throws ResourceNotFoundException {
        Beacon beacon = findOne(showId, id);
        beacon.setResourcesGroupId(null);
        return repository.save(beacon);
    }

    @Override
    public Beacon append(Long showId, InstallBeacon beacon) {
        Beacon savedBeacon = repository.findByShowIdAndBasicInfo_Id(showId, beacon.getId());
        if (null == savedBeacon) {
            savedBeacon = new Beacon();
        }
        savedBeacon.setShowId(showId);
        savedBeacon.setBasicInfo(beacon);
        savedBeacon.setUpdateTime(new Date());
        return repository.save(savedBeacon);
    }

    @Override
    public List<Beacon> append(Long showId, List<InstallBeacon> beacons) {
        if (null == beacons || beacons.isEmpty()) return new ArrayList<>();
        List<Long> ids = beacons.parallelStream().map(InstallBeacon::getId).collect(Collectors.toList());
        List<Beacon> savedBeacons = repository.findByShowIdAndBasicInfo_IdIn(showId, ids);
        Map<Long, Beacon> savedBeaconMap = new HashMap<>();
        for (Beacon beacon : savedBeacons) {
            if (null == beacon || null == beacon.getBasicInfo()) continue;
            savedBeaconMap.put(beacon.getBasicInfo().getId(), beacon);
        }
        Date date = new Date();
        List<Beacon> beaconList = new ArrayList<>(); // 最终持久化数据集
        for (InstallBeacon beacon : beacons) {
            Beacon bc = savedBeaconMap.get(beacon.getId());
            if (null == bc) bc = new Beacon();
            bc.setShowId(showId);
            bc.setBasicInfo(beacon);
            bc.setUpdateTime(date);
            beaconList.add(bc);
        }
        return repository.save(beaconList);
    }

    @Override
    public void remove(Long showId, InstallBeacon beacon) {
        Beacon savedBeacon = repository.findByShowIdAndBasicInfo_Id(showId, beacon.getId());
        if (null == savedBeacon) return;
        repository.delete(savedBeacon.getId());
    }
}
