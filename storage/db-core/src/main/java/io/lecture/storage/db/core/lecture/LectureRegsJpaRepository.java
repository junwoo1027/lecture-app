package io.lecture.storage.db.core.lecture;

import io.lecture.storage.db.core.lecture.entity.LectureRegsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRegsJpaRepository extends JpaRepository<LectureRegsEntity, Long> {

    LectureRegsEntity save(LectureRegsEntity lectureRegsEntity);

    boolean existsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId);

    int countByLectureId(Long lectureId);

    LectureRegsEntity findLectureRegsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId);

    List<LectureRegsEntity> findAllByLectureId(Long lectureId);

}
