package io.lecture.core.api.domain;

import org.springframework.stereotype.Service;

@Service
public class LectureService {
    private final LectureFinder lectureFinder;
    private final LectureAppender lectureAppender;

    public LectureService(LectureFinder lectureFinder, LectureAppender lectureAppender) {
        this.lectureFinder = lectureFinder;
        this.lectureAppender = lectureAppender;
    }

    public Long append(Lecture lecture) {
        return lectureAppender.append(lecture);
    }
}
