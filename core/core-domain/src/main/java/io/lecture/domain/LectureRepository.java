package io.lecture.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository {
    Long append(Lecture lecture);

    List<Lecture> findAll();

    Lecture findById(Long id);
}
