package io.lecture.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Long append(Lecture lecture) {
        return this.lectureRepository.append(lecture);
    }

    public List<Lecture> find() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekBefore = now.minusWeeks(1);
        LocalDateTime dayAfter = now.plusDays(1);
        return this.lectureRepository.find(weekBefore, dayAfter);
    }

    public List<Lecture> findAll() {
        return this.lectureRepository.findAll();
    }
}
