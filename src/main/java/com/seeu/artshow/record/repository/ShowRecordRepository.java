package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.ShowRecord;
import com.seeu.artshow.record.model.ShowRecordPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowRecordRepository extends JpaRepository<ShowRecord, ShowRecordPKeys> {

    List<ShowRecord> findAllByShowIdAndDayBetween(@Param("showId") Long showId, @Param("startDay") Integer startDay, @Param("endDay") Integer endDay);

}
