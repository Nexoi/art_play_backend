package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.ResourceRecord;
import com.seeu.artshow.record.model.ResourceRecordPKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRecordRepository extends JpaRepository<ResourceRecord, ResourceRecordPKeys> {
}
