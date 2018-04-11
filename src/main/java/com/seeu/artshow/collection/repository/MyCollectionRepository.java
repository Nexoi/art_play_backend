package com.seeu.artshow.collection.repository;

import com.seeu.artshow.collection.model.MyCollection;
import com.seeu.artshow.collection.model.MyCollectionPKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MyCollectionRepository extends JpaRepository<MyCollection, MyCollectionPKeys> {

    Page<MyCollection> findAllByUid(@Param("uid") Long uid, Pageable pageable);
}
