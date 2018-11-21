package com.company.appname.common.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.appname.common.config.exception.model.ApiError;
import com.company.appname.common.config.exception.model.ValidationException;
import com.company.appname.common.util.EventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return handleEntityNotFound(ex, headers, request);
    }

    @ExceptionHandler(RestClientException.class)
    protected ResponseEntity handleRestClientException(RestClientException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return handleRestClientException(ex, headers, request);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity handleValidationException(ValidationException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return handleValidationException(ex, headers, request);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity handleThrowable(Throwable ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return handleUnexpectedException(ex,headers, request);
    }

    @Nonnull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.handleMethodArgumentNotValidEx(ex, headers, request);
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<Object> handleMethodArgumentNotValidEx(MethodArgumentNotValidException ex, HttpHeaders headers, WebRequest request) {

        if (log.isErrorEnabled())
            log.error(ex.getMessage(), ex);

        ex.getBindingResult();

        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return this.handleValidationException(new ValidationException(EventMessage.INVALID_METHOD_ARGUMENTS, errors), headers, request);
    }

    private ResponseEntity handleEntityNotFound(EntityNotFoundException ex, HttpHeaders headers, WebRequest request) {

        if (log.isErrorEnabled())
            log.error(ex.getMessage(), ex);

        return this.handleExceptionInternal(ex, new ApiError(ex, HttpStatus.NOT_FOUND, request), headers, HttpStatus.NOT_FOUND, request);
    }

    private ResponseEntity handleUnexpectedException(Throwable ex, HttpHeaders headers, WebRequest request) {

        if (log.isErrorEnabled())
            log.error(ex.getMessage(), ex);

        request.setAttribute("technicalMessage", ex.getMessage(),0);

        ApiError apiError = new ApiError(ex, EventMessage.UNEXPECTED_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);

        return RestExceptionHandler.handleExceptionInternal(apiError, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity handleRestClientException(RestClientException ex, HttpHeaders headers, WebRequest request) {

        if (log.isErrorEnabled())
            log.error(ex.getMessage(), ex);

        if (ex instanceof HttpStatusCodeException) {

            HttpStatusCodeException httpClientError = ((HttpStatusCodeException) ex);

            String body = httpClientError.getResponseBodyAsString();

            try {

                ApiError apiError = objectMapper.readValue(body, ApiError.class);
                HttpStatus statusCode = HttpStatus.valueOf(apiError.getStatus());

                return this.handleExceptionInternal(ex, apiError, headers, statusCode, request);

            } catch (Exception e) {

                request.setAttribute("technicalMessage", EventMessage.UNEXPECTED_SERVER_ERROR.getMessage(), 0);

                HttpStatus statusCode = httpClientError.getStatusCode();
                ApiError apiError = new ApiError(ex, body, statusCode, request);

                return this.handleExceptionInternal(ex, apiError, headers, statusCode, request);
            }

        } else {

            HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
            ApiError apiError = new ApiError(ex, ex.getMessage(), internalServerError, request);

            return this.handleExceptionInternal(ex, apiError, headers, internalServerError, request);
        }

    }

    private ResponseEntity handleValidationException(ValidationException ex, HttpHeaders headers, WebRequest request) {

        if (log.isErrorEnabled())
            log.error(ex.getMessage(), ex);

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        request.setAttribute("technicalMessage", ex.getMessage(),0);

        ApiError apiError = new ApiError(ex, EventMessage.UNEXPECTED_SERVER_ERROR.getMessage(), badRequest, request);

        return this.handleExceptionInternal(ex, apiError, headers, badRequest, request);
    }

    private static ResponseEntity handleExceptionInternal(Object body, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(body, headers, status);
    }
}

