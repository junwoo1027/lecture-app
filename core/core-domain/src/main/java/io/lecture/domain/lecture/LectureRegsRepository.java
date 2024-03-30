package io.lecture.domain.lecture;

import org.springframework.stereotype.Repository;

@Repository
public interface LectureRegsRepository {
    Long apply(LectureRegs lectureRegs);

    boolean existsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId);

    int countByLectureId(Long lectureId);
}
