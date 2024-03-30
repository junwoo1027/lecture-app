package io.lecture.domain.lecture;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository {
    Long append(Lecture lecture);

    List<Lecture> findAll();

    Lecture findById(Long id);
}