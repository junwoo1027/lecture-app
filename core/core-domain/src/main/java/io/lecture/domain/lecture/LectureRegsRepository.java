package io.lecture.domain.lecture;

import io.lecture.domain.lecture.domain.LectureRegs;
import io.lecture.domain.lecture.domain.NewLectureRegs;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRegsRepository {
    Long apply(NewLectureRegs lectureRegs);

    boolean existsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId);

    int countByLectureId(Long lectureId);

    LectureRegs findLectureRegsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId);

    void cancel(Long lectureRegsId);

    List<LectureRegs> getLectureRegsListByLecture(Long lectureId);
}
