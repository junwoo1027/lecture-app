package io.lecture.core.api.support.error;

public class CoreApiException extends RuntimeException {

    private final CoreApiErrorType coreApiErrorType;

    private final Object data;

    public CoreApiException(CoreApiErrorType coreApiErrorType) {
        super(coreApiErrorType.getMessage());
        this.coreApiErrorType = coreApiErrorType;
        this.data = null;
    }

    public CoreApiException(CoreApiErrorType coreApiErrorType, Object data) {
        super(coreApiErrorType.getMessage());
        this.coreApiErrorType = coreApiErrorType;
        this.data = data;
    }

    public CoreApiErrorType getErrorType() {
        return coreApiErrorType;
    }

    public Object getData() {
        return data;
    }

}
