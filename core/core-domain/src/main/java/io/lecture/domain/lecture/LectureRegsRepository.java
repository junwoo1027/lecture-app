package io.lecture.domain.lecture;

import org.springframework.stereotype.Repository;

@Repository
public interface LectureRegsRepository {
    Long apply(NewLectureRegs lectureRegs);

    boolean existsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId);

    int countByLectureId(Long lectureId);

    LectureRegs findLectureRegsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId);

    void cancel(Long lectureRegsId);
}
