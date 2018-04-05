package com.seeu.artshow.installation.service;

import com.seeu.artshow.installation.model.InstallBeacon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;

public interface InstallBeaconService {
    Page<InstallBeacon> findAll(Pageable pageable);

    InstallBeacon add(@Validated InstallBeacon beacon);

    InstallBeacon update(Long id, @Validated InstallBeacon beacon);

    void delete(Long id);

    //... dispatcher

    void dispatch(Long showId, Collection<Long> beaconIds); // append

    void remove(Long showId, Long beaconId);

    Page<InstallBeacon> findAll(Long showId, Pageable pageable); // 查找该展览下的 beacon

    Page<InstallBeacon> findAllReverse(Long showId, Pageable pageable); // 查找没有分配给展览的 beacon

}
