package io.lecture.core.api.controller;

import io.lecture.core.api.support.error.CoreApiErrorType;
import io.lecture.core.api.support.error.CoreApiException;
import io.lecture.core.api.support.response.ApiResponse;
import io.lecture.domain.error.CoreErrorKind;
import io.lecture.domain.error.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CoreApiException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreApiException(CoreApiException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error("CoreApiException : {}", e.getMessage(), e);
            case WARN -> log.warn("CoreApiException : {}", e.getMessage(), e);
            default -> log.info("CoreApiException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getStatus());
    }

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreApiException(CoreException e) {
        switch (e.getErrorType().getCoreErrorLevel()) {
            case ERROR -> log.error("CoreException : {}", e.getMessage(), e);
            case WARNING -> log.warn("CoreException : {}", e.getMessage(), e);
            default -> log.info("CoreException : {}", e.getMessage(), e);
        }

        HttpStatus status;
        if (e.getErrorType().getKind() == CoreErrorKind.CLIENT_ERROR) {
            status = HttpStatus.BAD_REQUEST;
        }
        else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity(ApiResponse.error(e.getErrorType(), e.getData()), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(CoreApiErrorType.DEFAULT_ERROR),
                CoreApiErrorType.DEFAULT_ERROR.getStatus());
    }

}
