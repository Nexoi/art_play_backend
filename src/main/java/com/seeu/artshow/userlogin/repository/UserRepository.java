package com.seeu.artshow.userlogin.repository;

import com.seeu.artshow.userlogin.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhone(@Param("phone") String phone);

    User findByThirdPartName(@Param("username") String username);

    Page<User> findAllByUid(@Param("uid") Long uid, Pageable pageable);

    Page<User> findAllByPhoneLike(@Param("phone") String phoneLike, Pageable pageable);

    Page<User> findAllByUsernameLike(@Param("username") String usernameLike, Pageable pageable);

}