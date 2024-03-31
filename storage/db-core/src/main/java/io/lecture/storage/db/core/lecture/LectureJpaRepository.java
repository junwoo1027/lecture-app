package io.lecture.storage.db.core.lecture;

import io.lecture.storage.db.core.lecture.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {

    LectureEntity save(LectureEntity lectureEntity);

}
