package com.seeu.artshow.material.repository;

import com.seeu.artshow.material.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByShowId(@Param("showId") Long showId);

    List<Folder> findAllByName(@Param("name") String name);

    Folder findFirstByType(@Param("type") Folder.TYPE type);

    List<Folder> findAllByType(@Param("type") Folder.TYPE type);

    void deleteAllByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("delete from Folder f where f.showId = :showId")
    void deleteWithFolderShowId(@Param("showId") Long  showId);
}
