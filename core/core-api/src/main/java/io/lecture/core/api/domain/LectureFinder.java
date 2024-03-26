package io.lecture.core.api.domain;

import org.springframework.stereotype.Component;

@Component
public class LectureFinder {
    private final LectureRepository lectureRepository;

    public LectureFinder(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }
}
