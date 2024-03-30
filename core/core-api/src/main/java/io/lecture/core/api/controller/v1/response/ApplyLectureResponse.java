package io.lecture.core.api.controller.v1.response;

public record ApplyLectureResponse(Long id) {
    public static ApplyLectureResponse of(Long id) {
        return new ApplyLectureResponse(id);
    }
}
