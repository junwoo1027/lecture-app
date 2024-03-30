package io.lecture.domain;

import io.lecture.domain.error.CoreErrorType;
import io.lecture.domain.error.CoreException;

import io.lecture.redisson.aop.RedissonLock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureRegsRepository lectureRegsRepository;

    public LectureService(LectureRepository lectureRepository, LectureRegsRepository lectureRegsRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureRegsRepository = lectureRegsRepository;
    }

    public Long append(Lecture lecture) {
        return this.lectureRepository.append(lecture);
    }

    public List<Lecture> findAll() {
        return this.lectureRepository.findAll();
    }

    @RedissonLock(key = "#lockName")
    public Long apply(String lockName, LectureRegs lectureRegs) {
        isValidEmployeeNumber(lectureRegs.employeeNumber());
        Lecture lecture = findLecture(lectureRegs);
        checkIsApply(lectureRegs);
        checkIsExceeded(lectureRegs, lecture);
        return this.lectureRegsRepository.apply(lectureRegs);
    }

    private void isValidEmployeeNumber(Integer employeeNumber) {
        boolean validNumber = Integer.toString(employeeNumber).length() == 5;
        if (!validNumber) {
            throw new CoreException(CoreErrorType.NOT_VALID_EMPLOYEE_NUMBER);
        }
    }

    private Lecture findLecture(LectureRegs lectureRegs) {
        Lecture lecture = lectureRepository.findById(lectureRegs.lectureId());
        if (lecture == null) {
            throw new CoreException(CoreErrorType.NOT_FOUND_DATA);
        }
        return lecture;
    }

    private void checkIsApply(LectureRegs lectureRegs) {
        boolean isApply = lectureRegsRepository.existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(), lectureRegs.lectureId());
        if (isApply) {
            throw new CoreException(CoreErrorType.ALREADY_APPLIED_LECTURE);
        }
    }

    private void checkIsExceeded(LectureRegs lectureRegs, Lecture lecture) {
        int appliedCount = this.lectureRegsRepository.countByLectureId(lectureRegs.lectureId());
        lecture.isExceeded(appliedCount);
    }
}
