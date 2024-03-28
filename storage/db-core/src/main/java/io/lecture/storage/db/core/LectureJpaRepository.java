package io.lecture.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {
    List<LectureEntity> findByStartAtBetweenOrderByStartAt(LocalDateTime start, LocalDateTime end);
}
