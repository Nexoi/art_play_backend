package com.seeu.artshow.userlogin.repository;

import com.seeu.artshow.userlogin.model.UserAuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserAuthRoleRepository extends JpaRepository<UserAuthRole, Integer> {
    UserAuthRole findByName(@Param("name") String name);
}
