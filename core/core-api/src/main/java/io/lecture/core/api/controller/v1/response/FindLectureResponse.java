package io.lecture.core.api.controller.v1.response;

import io.lecture.domain.lecture.domain.Lecture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FindLectureResponse(
        String lecturer,
        String hall,
        int seats,
        LocalDateTime startAt,
        String description
) {
    public static List<FindLectureResponse> of(List<Lecture> lectures) {
        return lectures.stream().map(each -> new FindLectureResponse(each.lecturer(),
                        each.hall(),
                        each.seats(),
                        each.startAt(),
                        each.description()))
                .collect(Collectors.toList());
    }
}
