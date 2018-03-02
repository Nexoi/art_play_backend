package com.seeu.artshow.userlogin.exception;

public class NickNameSetException extends Exception {
    public NickNameSetException(String name) {
        super("NickName set exception [ " + name + " ]");
    }
}
