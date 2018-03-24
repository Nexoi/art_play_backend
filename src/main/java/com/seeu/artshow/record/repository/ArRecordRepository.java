package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.ArRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ArRecordRepository extends JpaRepository<ArRecord, Integer> {

    // 记录一次
    @Transactional
    @Modifying
    @Query("update ArRecord r set r.times = r.times + 1 where r.day = :day")
    void record(@Param("day") Integer day);

    List<ArRecord> findAllByDayBetween(@Param("startDay") Integer startDay, @Param("endDay") Integer endDay);

}
