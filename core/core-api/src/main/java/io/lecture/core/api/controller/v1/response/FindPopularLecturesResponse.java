package io.lecture.core.api.controller.v1.response;

import io.lecture.domain.lecture.domain.Lecture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FindPopularLecturesResponse(String lecturer, String hall, int seats, LocalDateTime startAt,
        String description) {
    public static List<FindPopularLecturesResponse> of(List<Lecture> lectures) {
        return lectures.stream()
            .map(each -> new FindPopularLecturesResponse(each.lecturer(), each.hall(), each.seats(), each.startAt(),
                    each.description()))
            .collect(Collectors.toList());
    }
}
