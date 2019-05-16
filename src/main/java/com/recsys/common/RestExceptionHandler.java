package com.recsys.common;

import com.recsys.controller.v1_1.response.BaseResponse;
import com.recsys.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    Logger log = LogManager.getLogger(this.getClass());

    @ExceptionHandler({Exception.class})
    ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        log.error("exception class: " + ex.getClass().getName());
        log.error("exception message: " + ex.getMessage());

        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements)
            log.error(stackTraceElement.toString());

        BaseResponse errorInfo;
        if (ex instanceof RestException) {
            RestException gpex = (RestException) ex;
            errorInfo = new BaseResponse();
            errorInfo.setResponseCode(gpex.getCode());
            errorInfo.setResponseMessage(gpex.getMessage());
        } else {
            errorInfo = new BaseResponse();
            errorInfo.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({InvalidParameterException.class, UnsupportedEncodingException.class})
    public ResponseEntity<Object> handleInvalidParameterException() {
        BaseResponse errorResp = new BaseResponse();
        errorResp.setResponseCode(ExceptionCode.INVALID_PARAMETER.getCode());
        errorResp.setResponseMessage(ExceptionCode.INVALID_PARAMETER.getMessage());
        return new ResponseEntity<>(errorResp, new HttpHeaders(), HttpStatus.OK);
    }


    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<Object> handleDataNotFoundException() {
        BaseResponse errorResp = new BaseResponse();
        errorResp.setResponseCode(ExceptionCode.NOT_FOUND_RESOURCE.getCode());
        errorResp.setResponseMessage(ExceptionCode.NOT_FOUND_RESOURCE.getMessage());
        return new ResponseEntity<>(errorResp, new HttpHeaders(), HttpStatus.OK);
    }


    @ExceptionHandler({InvalidQueryException.class, InvalidQueryFormatException.class, NumberFormatException.class})
    public ResponseEntity<Object> handleInvalidQuery() {
        BaseResponse errorResp = new BaseResponse();
        errorResp.setResponseCode(ExceptionCode.INVALID_QUERY_FORMAT.getCode());
        errorResp.setResponseMessage(ExceptionCode.INVALID_QUERY_FORMAT.getMessage());
        return new ResponseEntity<>(errorResp, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(DuplicatedDataException.class)
    public ResponseEntity<Object> handleDuplicatedDataException() {
        BaseResponse errorResp = new BaseResponse();
        errorResp.setResponseCode(ExceptionCode.DUPLICATED_DATA.getCode());
        errorResp.setResponseMessage(ExceptionCode.DUPLICATED_DATA.getMessage());
        return new ResponseEntity<>(errorResp, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(InvalidIncludePropertiesException.class)
    public ResponseEntity<Object> handleInvalidPropertyException(InvalidIncludePropertiesException ex) {
        BaseResponse errorResp = new BaseResponse();
        errorResp.setResponseCode(ExceptionCode.NOT_INCLUDED_PROPERTY.getCode());
        errorResp.setResponseMessage(ex.getMessage());
        return new ResponseEntity<>(errorResp, new HttpHeaders(), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseResponse errorResp = new BaseResponse();
        errorResp.setResponseCode(ExceptionCode.INVALID_PARAMETER.getCode());
        errorResp.setResponseMessage(ExceptionCode.INVALID_PARAMETER.getMessage());
        return new ResponseEntity<>(errorResp, new HttpHeaders(), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseResponse errorResp = new BaseResponse();
        errorResp.setResponseCode(ExceptionCode.PAGE_NOT_FOUND.getCode());
        errorResp.setResponseMessage(ExceptionCode.PAGE_NOT_FOUND.getMessage());
        return new ResponseEntity<>(errorResp, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseResponse errorResp = new BaseResponse();
        errorResp.setResponseCode(ExceptionCode.METHOD_NOT_ALLOWED.getCode());
        errorResp.setResponseMessage(ExceptionCode.METHOD_NOT_ALLOWED.getMessage());
        return new ResponseEntity<>(errorResp, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }
}

