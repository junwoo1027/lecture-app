package io.lecture.storage.db.core.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {

    LectureEntity save(LectureEntity lectureEntity);

}
