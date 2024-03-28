package io.lecture.core.api.controller.v1.response;

public record NewLectureResponse(Long id) {
    public static NewLectureResponse of(Long id) {
        return new NewLectureResponse(id);
    }
}
