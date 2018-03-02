package com.seeu.artshow.userlogin.exception;

public class AccountNameAlreadyExistedException extends Exception {
    public AccountNameAlreadyExistedException(String name) {
        super("Account Name [ " + name + " ] already existed!");
    }
}
