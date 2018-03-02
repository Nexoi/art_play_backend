package com.seeu.artshow.userlogin.service;

import com.seeu.artshow.userlogin.model.ThirdUserLogin;

public interface ThirdUserLoginService {
    ThirdUserLogin findByName(String name);

    boolean exists(String name);

    ThirdUserLogin save(ThirdUserLogin thirdUserLogin);

    @Deprecated
    ThirdUserLogin bindUid(String name, Long uid);

}
