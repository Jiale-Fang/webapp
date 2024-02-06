package pers.fjl.healthcheck.handler;

import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pers.fjl.healthcheck.entity.Result;

@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, RequestRejectedException.class})
    public ResponseEntity<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return Result.fail(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> validHandle(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            return Result.fail(HttpStatus.BAD_REQUEST, fieldError.getDefaultMessage());
        } else {
            return Result.fail(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleUnknownParams(HttpMessageNotReadableException ex) {
        return Result.fail(HttpStatus.BAD_REQUEST, "Request parameters are invalid");
    }

    /**
     * Can't connect to db
     */
    @ExceptionHandler(value = {PersistenceException.class, MyBatisSystemException.class})
    public ResponseEntity<Object> handleDBConnectionException() {
        return Result.fail(HttpStatus.SERVICE_UNAVAILABLE);
    }

}
