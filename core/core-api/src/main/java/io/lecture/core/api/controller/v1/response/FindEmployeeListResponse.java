package io.lecture.core.api.controller.v1.response;

import io.lecture.domain.lecture.LectureRegs;

import java.util.List;
import java.util.stream.Collectors;

public record FindEmployeeListResponse(
        int employeeNumber
) {
    public static List<FindEmployeeListResponse> of(List<LectureRegs> lectureRegsList) {
        return lectureRegsList.stream().map(each -> new FindEmployeeListResponse(each.employeeNumber())).collect(Collectors.toList());
    }
}
