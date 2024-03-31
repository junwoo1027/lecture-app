package io.lecture.core.api.support.error;

import io.lecture.domain.error.CoreErrorType;

public class CoreApiErrorMessage {

    private final String code;

    private final String message;

    private final Object data;

    public CoreApiErrorMessage(CoreApiErrorType coreApiErrorType) {
        this.code = coreApiErrorType.getCode().name();
        this.message = coreApiErrorType.getMessage();
        this.data = null;
    }

    public CoreApiErrorMessage(CoreApiErrorType coreApiErrorType, Object data) {
        this.code = coreApiErrorType.getCode().name();
        this.message = coreApiErrorType.getMessage();
        this.data = data;
    }

    public CoreApiErrorMessage(CoreErrorType coreErrorType) {
        this.code = coreErrorType.getCode().name();
        this.message = coreErrorType.getMessage();
        this.data = null;
    }

    public CoreApiErrorMessage(CoreErrorType coreErrorType, Object data) {
        this.code = coreErrorType.getCode().name();
        this.message = coreErrorType.getMessage();
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

}
