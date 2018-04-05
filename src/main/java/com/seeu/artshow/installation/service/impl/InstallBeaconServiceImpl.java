package com.seeu.artshow.installation.service.impl;

import com.seeu.artshow.installation.model.InstallBeacon;
import com.seeu.artshow.installation.repository.InstallBeaconRepository;
import com.seeu.artshow.installation.service.InstallBeaconService;
import com.seeu.artshow.show.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.service.BeaconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstallBeaconServiceImpl implements InstallBeaconService {
    @Resource
    private InstallBeaconRepository repository;
    @Autowired
    private BeaconService beaconService;

    @Override
    public Page<InstallBeacon> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public InstallBeacon add(InstallBeacon beacon) {
        beacon.setId(null);
        return repository.save(beacon);
    }

    @Override
    public InstallBeacon update(Long id, InstallBeacon beacon) {
        beacon.setId(id);
        return repository.save(beacon);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public void dispatch(Long showId, Collection<Long> beaconIds) {
        List<InstallBeacon> beaconList = repository.findAllByIdIn(beaconIds);
        if (beaconList.isEmpty()) return;
        beaconService.append(showId, beaconList);
    }

    @Override
    public void remove(Long showId, Long beaconId) {
        InstallBeacon beacon = repository.findOne(beaconId);
        if (null == beacon) return;
        beaconService.remove(showId, beacon);
    }

    @Override
    public Page<InstallBeacon> findAll(Long showId, Pageable pageable) {
        Page<Beacon> beaconPage = beaconService.findAllWithEmptyBeacons(showId, pageable);
        List<Beacon> beaconList = beaconPage.getContent();
        if (beaconList.isEmpty()) return new PageImpl<InstallBeacon>(new ArrayList<>());
        List<InstallBeacon> installBeacons = beaconList.parallelStream().map(Beacon::getBasicInfo).collect(Collectors.toList());
        return new PageImpl<InstallBeacon>(installBeacons, pageable, beaconPage.getTotalElements());
    }

    @Override
    public Page<InstallBeacon> findAllReverse(Long showId, Pageable pageable) {
        List<Beacon> beaconList = beaconService.findAllWithEmptyBeacons(showId);
        if (beaconList.isEmpty()) return findAll(pageable); // 把所有的 beacon 都丢出去
        List<InstallBeacon> installBeacons = beaconList.parallelStream().map(Beacon::getBasicInfo).collect(Collectors.toList());
        List<Long> installBeaconIds = installBeacons.parallelStream().map(InstallBeacon::getId).collect(Collectors.toList());
        if (installBeaconIds.isEmpty())
            return repository.findAll(pageable);
        return repository.findAllByIdNotIn(installBeaconIds, pageable);
    }
}
