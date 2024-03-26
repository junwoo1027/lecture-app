package io.lecture.core.api.controller.v1.request;

import io.lecture.core.api.domain.Lecture;

import java.time.LocalDateTime;

public record NewLectureRequest(
        String lecturer,
        String hall,
        int seats,
        LocalDateTime startAt,
        String description
) {
    public Lecture toLecture() {
        return new Lecture(
                this.lecturer,
                this.hall,
                this.seats,
                this.startAt,
                this.description
        );
    }
}
