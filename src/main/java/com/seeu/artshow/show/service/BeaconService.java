package com.seeu.artshow.show.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.InstallBeacon;
import com.seeu.artshow.show.model.Beacon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 */
public interface BeaconService {

    Beacon findOne(Long showId, String uuid) throws ResourceNotFoundException;

    List<Beacon> findAll(Long showId, Collection<String> uuids);

    Page<Beacon> findAll(Long showId, Pageable pageable);

    Beacon update(Long showId, String uuid, Beacon beacon) throws ActionParameterException, ResourceNotFoundException;

    Beacon changeStatus(Long showId, String uuid) throws ResourceNotFoundException; // 改变状态（ on / off ）

    Beacon removeBindInfo(Long showId, String uuid) throws ResourceNotFoundException;

    // adminx
    // 新增的 beacon 不会将原来的数据覆盖 save 掉
    Beacon append(Long showId, InstallBeacon beacon);

    List<Beacon> append(Long showId, List<InstallBeacon> beacons);

    void remove(Long showId, InstallBeacon beacon);
}
