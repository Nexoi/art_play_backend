package com.seeu.artshow.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String label, String value) {
        super("Resource Not Found [ " + label + " : " + value + " ]");
    }
}
