package com.seeu.artshow.userlogin.service;

public interface ThirdPartTokenService {
    public interface Processor {
        public void process(boolean isValidated, String username, String nickname, String headIconUrl);
    }

    void validatedInfo(TYPE type, String username, String token, Processor processor);

    public enum TYPE{
        WeChat,
        Weibo
    }
}
