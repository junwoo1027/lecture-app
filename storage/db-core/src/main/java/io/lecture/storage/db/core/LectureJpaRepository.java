package io.lecture.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {
}
