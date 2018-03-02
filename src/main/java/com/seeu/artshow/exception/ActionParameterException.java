package com.seeu.artshow.exception;

public class ActionParameterException extends Exception {
    public ActionParameterException(String parameter) {
        super("传入参数错误：" + parameter);
    }
}
