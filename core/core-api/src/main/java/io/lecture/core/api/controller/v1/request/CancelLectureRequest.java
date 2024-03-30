package io.lecture.core.api.controller.v1.request;

import io.lecture.domain.lecture.CancelLectureRegs;
import jakarta.validation.constraints.NotNull;

public record CancelLectureRequest(
        @NotNull Integer employeeNumber
) {
    public CancelLectureRegs toCancelLectureRegs(Long lectureId) {
        return new CancelLectureRegs(
                this.employeeNumber,
                lectureId
        );
    }
}