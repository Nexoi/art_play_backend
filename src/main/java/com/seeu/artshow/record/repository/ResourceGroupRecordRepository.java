package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.ResourceGroupRecord;
import com.seeu.artshow.record.model.ResourceGroupRecordPKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceGroupRecordRepository extends JpaRepository<ResourceGroupRecord,ResourceGroupRecordPKeys>{
}
