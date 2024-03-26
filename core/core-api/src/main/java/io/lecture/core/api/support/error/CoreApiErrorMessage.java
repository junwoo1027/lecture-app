package io.lecture.core.api.support.error;

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
