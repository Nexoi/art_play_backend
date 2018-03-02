package com.seeu.artshow.userlogin.exception;

public class NoSuchUserException extends Exception {
    public NoSuchUserException(Long uid, String phone) {
        super("用户【UID：" + uid + ", Phone: " + phone + "】不存在");
    }
}
