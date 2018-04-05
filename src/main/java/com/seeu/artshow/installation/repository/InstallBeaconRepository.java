package com.seeu.artshow.installation.repository;

import com.seeu.artshow.installation.model.InstallBeacon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface InstallBeaconRepository extends JpaRepository<InstallBeacon, Long> {
    List<InstallBeacon> findAllByIdIn(@Param("ids") Collection<Long> ids);
}
