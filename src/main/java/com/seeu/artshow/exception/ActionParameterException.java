package com.seeu.artshow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ActionParameterException extends Exception {
    public ActionParameterException(String message) {
        super(message);
    }
}
