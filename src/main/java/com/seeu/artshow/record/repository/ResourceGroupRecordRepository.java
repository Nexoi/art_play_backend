package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.ResourceGroupRecord;
import com.seeu.artshow.record.model.ResourceGroupRecordPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceGroupRecordRepository extends JpaRepository<ResourceGroupRecord, ResourceGroupRecordPKeys> {

    List<ResourceGroupRecord> findAllByGroupIdAndDayBetween(@Param("groupId") Long groupId, @Param("startDay") Integer startDay, @Param("endDay") Integer endDay);

}
