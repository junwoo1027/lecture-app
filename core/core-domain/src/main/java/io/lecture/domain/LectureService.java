package io.lecture.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {
    private final LectureFinder lectureFinder;
    private final LectureProcessor lectureProcessor;

    public LectureService(LectureFinder lectureFinder, LectureProcessor lectureProcessor) {
        this.lectureFinder = lectureFinder;
        this.lectureProcessor = lectureProcessor;
    }

    public Long append(Lecture lecture) {
        return this.lectureProcessor.append(lecture);
    }

    public List<Lecture> find() {
        return this.lectureFinder.find();
    }

    public List<Lecture> findAll() {
        return this.lectureFinder.findAll();
    }

}
