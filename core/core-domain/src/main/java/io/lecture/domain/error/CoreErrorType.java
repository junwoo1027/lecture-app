package io.lecture.domain.error;

public enum CoreErrorType {
    NOT_FOUND_DATA(CoreErrorKind.SERVER_ERROR, CoreErrorCode.E1000, "Not found data.", CoreErrorLevel.INFO);

    private final CoreErrorKind kind;
    private final CoreErrorCode code;
    private final String message;
    private final CoreErrorLevel coreErrorLevel;

    CoreErrorType(CoreErrorKind kind, CoreErrorCode code, String message, CoreErrorLevel coreErrorLevel) {
        this.kind = kind;
        this.code = code;
        this.message = message;
        this.coreErrorLevel = coreErrorLevel;
    }

    public CoreErrorKind getKind() {
        return kind;
    }

    public CoreErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public CoreErrorLevel getCoreErrorLevel() {
        return coreErrorLevel;
    }
}