package io.lecture.domain.lecture;

import io.lecture.domain.lecture.domain.Lecture;
import io.lecture.domain.lecture.domain.NewLecture;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureRepository {

    Long append(NewLecture lecture);

    List<Lecture> findAll();

    Lecture findById(Long id);

    List<Lecture> findLecturesByEmployee(int employeeNumber);

    List<Lecture> findPopularLectures(LocalDateTime dateTime);

}
