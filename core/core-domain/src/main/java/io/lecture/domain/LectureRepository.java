package io.lecture.domain;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureRepository {
    Long append(Lecture lecture);

    List<Lecture> find(LocalDateTime start, LocalDateTime end);

    List<Lecture> findAll();
}
