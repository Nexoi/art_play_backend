package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.QRCodeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QRCodeRecordRepository extends JpaRepository<QRCodeRecord,Integer>{

    List<QRCodeRecord> findAllByDayBetween(@Param("startDay") Integer startDay, @Param("endDay") Integer endDay);

}
