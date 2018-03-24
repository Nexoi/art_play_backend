package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.BeaconRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BeaconRecordRepository extends JpaRepository<BeaconRecord, Integer> {

    List<BeaconRecord> findAllByDayBetween(@Param("startDay") Integer startDay, @Param("endDay") Integer endDay);

}
