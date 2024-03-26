package io.lecture.core.api.domain;

import org.springframework.stereotype.Component;

@Component
public class LectureAppender {
    private final LectureRepository lectureRepository;

    public LectureAppender(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Long append(Lecture lecture) {
        return lectureRepository.append(lecture);
    }
}
