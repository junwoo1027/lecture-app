package io.lecture.domain.lecture.domain;

import io.lecture.domain.error.CoreErrorType;
import io.lecture.domain.error.CoreException;

import java.time.LocalDateTime;

public record NewLecture(
        String lecturer,
        String hall,
        int seats,
        LocalDateTime startAt,
        String description
) {
}
