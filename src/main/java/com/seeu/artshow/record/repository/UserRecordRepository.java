package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecordRepository extends JpaRepository<UserRecord, Integer> {
}
