package io.lecture.storage.db.core.lecture;

import io.lecture.domain.lecture.domain.Lecture;
import io.lecture.domain.lecture.LectureRepository;
import io.lecture.domain.lecture.domain.NewLecture;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LectureCoreRepository implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;

    private final LectureQueryDslRepository lectureQueryDslRepository;

    public LectureCoreRepository(LectureJpaRepository lectureJpaRepository,
            LectureQueryDslRepository lectureQueryDslRepository) {
        this.lectureJpaRepository = lectureJpaRepository;
        this.lectureQueryDslRepository = lectureQueryDslRepository;
    }

    @Override
    public Long append(NewLecture lecture) {
        return this.lectureJpaRepository
            .save(new LectureEntity(lecture.lecturer(), lecture.hall(), lecture.seats(), lecture.startAt(),
                    lecture.description()))
            .getId();
    }

    @Override
    public List<Lecture> findAll() {
        return this.lectureJpaRepository.findAll().stream().map(LectureEntity::toLecture).collect(Collectors.toList());
    }

    @Override
    public Lecture findById(Long id) {
        LectureEntity lectureEntity = this.lectureJpaRepository.findById(id).orElse(null);
        if (lectureEntity != null) {
            return lectureEntity.toLecture();
        }
        else {
            return null;
        }
    }

    @Override
    public List<Lecture> findLecturesByEmployee(int employeeNumber) {
        return this.lectureQueryDslRepository.findLecturesByEmployee(employeeNumber)
            .stream()
            .map(LectureEntity::toLecture)
            .collect(Collectors.toList());
    }

    @Override
    public List<Lecture> findPopularLectures(LocalDateTime dateTime) {
        return this.lectureQueryDslRepository.findPopularLectures(dateTime)
            .stream()
            .map(LectureEntity::toLecture)
            .collect(Collectors.toList());
    }

}
