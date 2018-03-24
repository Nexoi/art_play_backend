package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.ResourceRecord;
import com.seeu.artshow.record.model.ResourceRecordPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRecordRepository extends JpaRepository<ResourceRecord, ResourceRecordPKeys> {

    List<ResourceRecord> findAllByResourceIdAndDayBetween(@Param("resourceId") Long resourceId, @Param("startDay") Integer startDay, @Param("endDay") Integer endDay);

}
