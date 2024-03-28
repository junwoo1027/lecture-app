package io.lecture.domain;

import org.springframework.stereotype.Component;

@Component
public class LectureProcessor {
    private final LectureRepository lectureRepository;

    public LectureProcessor(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Long append(Lecture lecture) {
         return lectureRepository.append(lecture);
    }
}
