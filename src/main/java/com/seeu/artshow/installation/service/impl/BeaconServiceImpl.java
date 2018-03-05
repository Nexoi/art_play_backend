package com.seeu.artshow.installation.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.Beacon;
import com.seeu.artshow.installation.repository.BeaconRepository;
import com.seeu.artshow.installation.service.BeaconService;
import com.seeu.artshow.show.model.ResourceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class BeaconServiceImpl implements BeaconService {
    @Resource
    private BeaconRepository repository;

    @Override
    public Beacon findOne(String uuid) throws ResourceNotFoundException {
        Beacon beacon = repository.findOne(uuid);
        if (beacon == null) throw new ResourceNotFoundException("beacon", uuid);
        return beacon;
    }

    @Override
    public List<Beacon> findAll(Collection<String> uuids) {
        if (uuids != null && !uuids.isEmpty())
            return repository.findAllByUuidIn(uuids);
        return new ArrayList<>();
    }

    @Override
    public List<Beacon> findAll(Long groupId) {
        return repository.findAllByResourcesGroupId(groupId);
    }

    @Override
    public Page<Beacon> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Beacon> findAllByResourceGroupIds(Collection<Long> resourceGroupIds, Pageable pageable) {
        return repository.findAllByResourcesGroupIdIn(resourceGroupIds, pageable);
    }

    @Override
    public Beacon add(@Validated Beacon beacon) throws ActionParameterException {
        if (beacon == null || beacon.getUuid() == null) throw new ActionParameterException("传入参数不完整【Beacon】");
        // 查看有木有
        Beacon b = repository.findOne(beacon.getUuid());
        if (b != null)
            throw new ActionParameterException("已经存在该 Beacon 【 " + b.getUuid() + " 】了，不可重复添加");
        beacon.setUpdateTime(new Date());
        return repository.save(beacon);
    }

    @Override
    public Beacon update(Beacon beacon) throws ActionParameterException, ResourceNotFoundException {
        String uuid = beacon.getUuid();
        if (uuid == null) throw new ActionParameterException("传入参数不完整【Beacon】");
        Beacon savedBeacon = findOne(uuid);
        if (beacon.getAvailableRange() != null) savedBeacon.setAvailableRange(beacon.getAvailableRange());
        if (beacon.getMajorValue() != null) savedBeacon.setMajorValue(beacon.getMajorValue());
        if (beacon.getMinorValue() != null) savedBeacon.setMinorValue(beacon.getMinorValue());
        if (beacon.getName() != null) savedBeacon.setName(beacon.getName());
        if (beacon.getPositionHeight() != null && beacon.getPositionWidth() != null) {
            savedBeacon.setPositionHeight(beacon.getPositionHeight());
            savedBeacon.setPositionWidth(beacon.getPositionWidth());
        }
        if (beacon.getResourcesGroupId() != null) savedBeacon.setResourcesGroupId(beacon.getResourcesGroupId());
        if (beacon.getShowMap() != null) savedBeacon.setShowMap(beacon.getShowMap());
        if (beacon.getStatus() != null) savedBeacon.setStatus(beacon.getStatus());
        savedBeacon.setUpdateTime(new Date());
        return repository.save(savedBeacon);
    }

    @Override
    public Beacon changeStatus(String uuid) throws ResourceNotFoundException {
        Beacon beacon = findOne(uuid);
        Beacon.STATUS status = beacon.getStatus();
        beacon.setStatus(Beacon.STATUS.on == status ? Beacon.STATUS.off : Beacon.STATUS.on);
        return repository.save(beacon);
    }

    @Override
    public void delete(String uuid) {
        repository.delete(uuid);
    }

    @Override
    public void delete(Collection<String> uuids) {
        if (uuids != null && !uuids.isEmpty())
            repository.deleteAllByUuidIn(uuids);
    }
}
