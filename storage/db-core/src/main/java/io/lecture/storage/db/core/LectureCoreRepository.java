package io.lecture.storage.db.core;

import io.lecture.domain.lecture.Lecture;
import io.lecture.domain.lecture.LectureRepository;
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
        return this.lectureJpaRepository.save(
                new LectureEntity(lecture.lecturer(), lecture.hall(), lecture.seats(), lecture.startAt(), lecture.description())
        ).getId();
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
        } else {
            return null;
        }
    }
}
