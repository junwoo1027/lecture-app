package io.lecture.core.api.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum CoreApiErrorType {

    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, CoreApiErrorCode.E500, "An unexpected error has occurred.",
            LogLevel.ERROR);

    private final HttpStatus status;

    private final CoreApiErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    CoreApiErrorType(HttpStatus status, CoreApiErrorCode code, String message, LogLevel logLevel) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public CoreApiErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
