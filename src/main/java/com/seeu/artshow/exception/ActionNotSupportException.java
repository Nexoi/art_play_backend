package com.seeu.artshow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 操作不支持
 * 比如：
 * 未点赞但进行取消点赞操作
 * 未关注但进行取消关注操作
 */

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ActionNotSupportException extends Exception {
    public ActionNotSupportException(String message) {
        super(message);
    }
}
