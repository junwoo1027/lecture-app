package io.lecture.storage.db.core.lecture;

import io.lecture.domain.lecture.LectureRegs;
import io.lecture.domain.lecture.LectureRegsRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LectureRegsCoreRepository implements LectureRegsRepository {
    private final LectureRegsJpaRepository lectureRegsJpaRepository;

    public LectureRegsCoreRepository(LectureRegsJpaRepository lectureRegsJpaRepository) {
        this.lectureRegsJpaRepository = lectureRegsJpaRepository;
    }

    @Override
    public Long apply(LectureRegs lectureRegs) {
        return this.lectureRegsJpaRepository.save(
                new LectureRegsEntity(lectureRegs.employeeNumber(), lectureRegs.lectureId())
        ).getId();
    }

    @Override
    public boolean existsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId) {
        return this.lectureRegsJpaRepository.existsByEmployeeNumberAndLectureId(employeeNumber, lectureId);
    }

    @Override
    public int countByLectureId(Long lectureId) {
        return this.lectureRegsJpaRepository.countByLectureId(lectureId);
    }
}
