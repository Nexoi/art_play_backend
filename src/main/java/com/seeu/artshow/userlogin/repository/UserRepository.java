package com.seeu.artshow.userlogin.repository;

import com.seeu.artshow.userlogin.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhone(@Param("phone") String phone);

    User findByThirdPartName(@Param("username") String username);

    User findByNickname(@Param("nickname") String nickname);

    Page<User> findAllByUid(@Param("uid") Long uid, Pageable pageable);

    Page<User> findAllByPhoneLike(@Param("phone") String phoneLike, Pageable pageable);

    Page<User> findAllByNicknameLikeOrPhoneLike(@Param("username") String username, @Param("phone") String phone, Pageable pageable);

    // adminX
    Page<User> findAllByType(@Param("type") User.TYPE type, Pageable pageable);

//    Page<User> findAllByTypeAndNicknameLikeOrPhoneLike(@Param("type") User.TYPE type, @Param("username") String username, @Param("phone") String phone, Pageable pageable);

    Page<User> findAllByTypeAndNicknameLikeOrTypeAndPhoneLike(@Param("type") User.TYPE type, @Param("username") String username, @Param("type2") User.TYPE type2, @Param("phone") String phone, Pageable pageable);

}