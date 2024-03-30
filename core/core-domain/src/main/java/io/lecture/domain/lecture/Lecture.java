package io.lecture.domain.lecture;

import io.lecture.domain.error.CoreErrorType;
import io.lecture.domain.error.CoreException;

import java.time.LocalDateTime;

public record Lecture(
        String lecturer,
        String hall,
        int seats,
        LocalDateTime startAt,
        String description
) {
    public void isExceeded(int count) {
        if (this.seats <= count) {
            throw new CoreException(CoreErrorType.LECTURE_EXCEEDED);
        }
    }
}
