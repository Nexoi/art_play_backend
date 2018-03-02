package com.seeu.artshow.userlogin.exception;

public class PhoneNumberHasUsedException extends Exception {
    public PhoneNumberHasUsedException(String phone) {
        super("Phone number has been used! [Phone " + phone + " ]");
    }
}
