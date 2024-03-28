package io.lecture.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LectureFinder {
    private final LectureRepository lectureRepository;

    public LectureFinder(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<Lecture> find() {
        return this.lectureRepository.find();
    }

    public List<Lecture> findAll() {
        return this.lectureRepository.findAll();
    }
}
