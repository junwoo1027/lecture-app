package io.lecture.core.api.controller.v1.request;

import io.lecture.domain.lecture.LectureRegs;
import jakarta.validation.constraints.NotNull;

public record ApplyLectureRequest(
        @NotNull Integer employeeNumber
) {
    public LectureRegs toLectureRegs(Long lectureId) {
        return new LectureRegs(
                this.employeeNumber,
                lectureId
        );
    }
}