package io.lecture.core.api.domain;

import java.time.LocalDateTime;

public record Lecture(
        String lecturer,
        String hall,
        int seats,
        LocalDateTime startAt,
        String description
) {
}
