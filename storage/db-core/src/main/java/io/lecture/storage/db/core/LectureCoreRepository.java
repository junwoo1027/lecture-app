package io.lecture.storage.db.core;

import io.lecture.core.api.domain.Lecture;
import io.lecture.core.api.domain.LectureRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LectureCoreRepository implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;

    public LectureCoreRepository(LectureJpaRepository lectureJpaRepository) {
        this.lectureJpaRepository = lectureJpaRepository;
    }

    @Override
    public Long append(Lecture lecture) {
        return lectureJpaRepository.save(
                new LectureEntity(lecture.lecturer(), lecture.hall(), lecture.seats(), lecture.startAt(), lecture.description())
        ).getId();
    }

    @Override
    public List<Lecture> findAll() {
        return lectureJpaRepository.findAll().stream().map(LectureEntity::toLecture).collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        lectureJpaRepository.deleteAll();
    }
}
