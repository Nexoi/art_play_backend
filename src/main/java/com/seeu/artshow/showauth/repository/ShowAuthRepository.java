package com.seeu.artshow.showauth.repository;

import com.seeu.artshow.showauth.model.ShowAuth;
import com.seeu.artshow.showauth.model.ShowAuthPKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowAuthRepository extends JpaRepository<ShowAuth, ShowAuthPKeys> {
    List<ShowAuth> findAllByUid(@Param("uid") Long uid);
}
