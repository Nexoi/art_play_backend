package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.ShowRecord;
import com.seeu.artshow.record.model.ShowRecordPKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRecordRepository extends JpaRepository<ShowRecord,ShowRecordPKeys>{
}
