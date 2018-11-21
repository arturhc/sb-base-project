package com.company.appname.common.config.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

import lombok.Data;

@Data
public class ApiError {

    private long timestamp;
    private int status;
    private String error;
    private String exception;
    private Object message;
    private String technicalMessage;
    private String path;

    private ApiError() {
        timestamp = Instant.now().toEpochMilli();
    }

    public ApiError(Throwable ex, HttpStatus status, WebRequest request) {
        this();
        this.status = status.value();
        this.error  = status.getReasonPhrase();
        this.exception = ex.getClass().getTypeName();
        this.message = ex.getMessage();
        this.path = request.getDescription(false);
    }

    public ApiError(Throwable ex, String message, HttpStatus status, WebRequest request) {
        this();
        this.status = status.value();
        this.error  = status.getReasonPhrase();
        this.exception = ex.getClass().getTypeName();
        this.message = message;
        this.technicalMessage = ex.getMessage();
        this.path = request.getDescription(false);
    }

    public ApiError(ValidationException ex, String message, HttpStatus status, WebRequest request) {
        this();
        this.status = status.value();
        this.error  = status.getReasonPhrase();
        this.exception = ex.getClass().getTypeName();
        this.message = StringUtils.isEmpty(ex.getBody()) ? message : ex.getBody();
        this.technicalMessage = ex.getMessage();
        this.path = request.getDescription(false);
    }

}

