package com.seeu.artshow.showauth.repository;

import com.seeu.artshow.showauth.model.ShowAuth;
import com.seeu.artshow.showauth.model.ShowAuthPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ShowAuthRepository extends JpaRepository<ShowAuth, ShowAuthPKeys> {
    List<ShowAuth> findAllByUid(@Param("uid") Long uid);

//    void deleteAllByUid(@Param("uid") Long uid);

    @Transactional
    @Modifying
    @Query("delete from ShowAuth s where s.uid = :uid")
    void deleteAuths(@Param("uid") Long uid);

}
