package com.seeu.artshow.record.repository;

import com.seeu.artshow.record.model.UserRecordCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRecordCacheRepository extends JpaRepository<UserRecordCache, String> {
    Long countAllByType(@Param("type")UserRecordCache.TYPE type);
}
