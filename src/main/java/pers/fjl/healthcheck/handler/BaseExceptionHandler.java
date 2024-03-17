package pers.fjl.healthcheck.handler;

import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import pers.fjl.healthcheck.service.impl.HealthCheckServiceImpl;

@ControllerAdvice
public class BaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckServiceImpl.class);
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        logger.error("The server has encountered an exception, current database may not available.", ex);
        return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR, "The server has encountered an exception, please check if the current database is available.");
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, RequestRejectedException.class})
    public ResponseEntity<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        logger.warn("Illegal http method", ex);
        return Result.fail(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> validHandle(MethodArgumentNotValidException ex) {
        logger.warn("Illegal argument", ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            return Result.fail(HttpStatus.BAD_REQUEST, fieldError.getDefaultMessage());
        } else {
            return Result.fail(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleUnknownParams(HttpMessageNotReadableException ex) {
        logger.warn("Contains unknown params", ex);
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
