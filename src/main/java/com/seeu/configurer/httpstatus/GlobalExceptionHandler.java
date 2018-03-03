package com.seeu.configurer.httpstatus;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.file.storage.StorageFileNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 * <p>
 * Created by neo on 24/09/2017.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StorageFileNotFoundException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "无此文件")
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到该资源");
    }

//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<?> handleNullPointerException(NullPointerException exc) {
//        return ResponseEntity.badRequest().body("空指针异常");// TODO
//    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException exc) {
        return ResponseEntity.badRequest().body("请确认传入参数是否完整 [ " + exc.getMessage() + " ]");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exc) {
        return ResponseEntity.badRequest().body("请确认传入参数类型是否正确 [ " + exc.getMessage() + " ]");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("接口 {[" + exc.getHttpMethod() + "]" + exc.getRequestURL() + "} 不存在");
    }

    // 数据库 unique 值重复异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException exc) {
        return ResponseEntity.badRequest().body("不支持该请求类型 [" + request.getMethod() + "]");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleOtherException(HttpServletRequest request, DataIntegrityViolationException exc) {
        if (exc.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException throwable = (ConstraintViolationException) exc.getCause();
            return ResponseEntity.badRequest().body("参数值重复，已存在过该记录，数据存储失败 【参数描述： " + throwable.getConstraintName() + " 】");
        }
        return ResponseEntity.badRequest().body("参数值重复，已存在过该记录，数据存储失败");
    }

    @ExceptionHandler(ActionParameterException.class)
    public ResponseEntity<?> handleActionParameterException(HttpServletRequest request, ActionParameterException exc) {
        return ResponseEntity.status(400).body(exc.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException exc) {
        return ResponseEntity.status(404).body("无此资源");
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleEmptyResultDataAccessException(HttpServletRequest request, EmptyResultDataAccessException exc) {
        return ResponseEntity.status(404).body("无此资源");
    }
}
