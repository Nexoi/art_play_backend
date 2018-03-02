package com.seeu.artshow.userlogin.service;

import com.seeu.artshow.userlogin.model.ThirdUserLogin;

public interface ThirdPartTokenService {
    public interface Processor {
        public void process(boolean isValidated, String username, String nickname, String headIconUrl);
    }

    void validatedInfo(ThirdUserLogin.TYPE type, String username, String token, Processor processor);
}
