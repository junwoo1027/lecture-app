package io.lecture.storage.db.core.lectureregs;

import io.lecture.domain.lecture.domain.LectureRegs;
import io.lecture.domain.lecture.LectureRegsRepository;
import io.lecture.domain.lecture.domain.NewLectureRegs;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LectureRegsCoreRepository implements LectureRegsRepository {

    private final LectureRegsJpaRepository lectureRegsJpaRepository;

    public LectureRegsCoreRepository(LectureRegsJpaRepository lectureRegsJpaRepository) {
        this.lectureRegsJpaRepository = lectureRegsJpaRepository;
    }

    @Override
    public Long apply(NewLectureRegs lectureRegs) {
        return this.lectureRegsJpaRepository
            .save(new LectureRegsEntity(lectureRegs.employeeNumber(), lectureRegs.lectureId()))
            .getId();
    }

    @Override
    public boolean existsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId) {
        return this.lectureRegsJpaRepository.existsByEmployeeNumberAndLectureId(employeeNumber, lectureId);
    }

    @Override
    public int countByLectureId(Long lectureId) {
        return this.lectureRegsJpaRepository.countByLectureId(lectureId);
    }

    @Override
    public LectureRegs findLectureRegsByEmployeeNumberAndLectureId(int employeeNumber, Long lectureId) {
        return this.lectureRegsJpaRepository.findLectureRegsByEmployeeNumberAndLectureId(employeeNumber, lectureId)
            .toLectureRegs();
    }

    @Transactional
    @Override
    public void cancel(Long lectureRegsId) {
        this.lectureRegsJpaRepository.deleteById(lectureRegsId);
    }

    @Override
    public List<LectureRegs> getLectureRegsListByLecture(Long lectureId) {
        return this.lectureRegsJpaRepository.findAllByLectureId(lectureId)
            .stream()
            .map(LectureRegsEntity::toLectureRegs)
            .collect(Collectors.toList());
    }

}
