package io.lecture.domain;

import java.time.LocalDateTime;

public record Lecture(
        String lecturer,
        String hall,
        int seats,
        LocalDateTime startAt,
        String description
) {
}
