package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRecordRepository extends JpaRepository<UserRecord, Integer> {

    List<UserRecord> findAllByTypeAndDayBetween(@Param("type") UserRecord.TYPE type, @Param("startDay") Integer startDay, @Param("endDay") Integer endDay);

}
