package io.lecture.core.api.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository {
    Long append(Lecture lecture);

    List<Lecture> findAll();

    void deleteAll();
}
