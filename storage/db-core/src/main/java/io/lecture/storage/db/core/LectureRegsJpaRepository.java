package io.lecture.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRegsJpaRepository extends JpaRepository<LectureRegsEntity, Long> {
    LectureRegsEntity save(LectureRegsEntity lectureRegsEntity);

    boolean existsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId);

    int countByLectureId(Long lectureId);
}
