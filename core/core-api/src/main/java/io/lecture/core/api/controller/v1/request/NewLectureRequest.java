package io.lecture.core.api.controller.v1.request;

import io.lecture.domain.lecture.NewLecture;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NewLectureRequest(
        @NotNull String lecturer,
        @NotNull String hall,
        @NotNull Integer seats,
        @NotNull LocalDateTime startAt,
        @NotNull String description
) {
    public NewLecture toNewLecture() {
        return new NewLecture(
                this.lecturer,
                this.hall,
                this.seats,
                this.startAt,
                this.description
        );
    }
}
