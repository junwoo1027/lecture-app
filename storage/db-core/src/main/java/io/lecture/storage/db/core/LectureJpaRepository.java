package io.lecture.storage.db.core;

import io.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {
    LectureEntity save(LectureEntity lectureEntity);
}
