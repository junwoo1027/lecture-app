package io.lecture.domain.error;

public class CoreException extends RuntimeException {

    private final CoreErrorType errorType;

    private final Object data;

    public CoreException(CoreErrorType errorType) {
        this(errorType, null);
    }

    public CoreException(CoreErrorType errorType, Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }

    public CoreErrorType getErrorType() {
        return errorType;
    }

    public Object getData() {
        return data;
    }

}