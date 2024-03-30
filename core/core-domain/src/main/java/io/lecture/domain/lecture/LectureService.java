package io.lecture.domain.lecture;

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

    public List<Lecture> findLectures() {
        return this.lectureRepository.findAll();
    }

    @RedissonLock(key = "#lockName")
    public Long apply(String lockName, NewLectureRegs lectureRegs) {
        this.isValidEmployeeNumber(lectureRegs.employeeNumber());
        Lecture lecture = findLecture(lectureRegs.lectureId());

        boolean isExistsRegs = this.existsLectureRegs(lectureRegs.employeeNumber(), lectureRegs.lectureId());
        if (isExistsRegs) {
            throw new CoreException(CoreErrorType.ALREADY_APPLIED_LECTURE);
        }

        this.checkIsExceeded(lectureRegs, lecture);
        return this.lectureRegsRepository.apply(lectureRegs);
    }

    public void cancel(CancelLectureRegs cancelLectureRegs) {
        LectureRegs lectureRegs = findLectureRegistrationsByLecture(cancelLectureRegs.employeeNumber(), cancelLectureRegs.lectureId());
        this.lectureRegsRepository.cancel(lectureRegs.id());
    }

    public List<LectureRegs> findLectureRegistrationsByLecture(Long lectureId) {
        return this.lectureRegsRepository.getLectureRegsListByLecture(lectureId);
    }

    private LectureRegs findLectureRegistrationsByLecture(int employeeNumber, Long lectureId) {
        LectureRegs lectureRegs = this.lectureRegsRepository.findLectureRegsByEmployeeNumberAndLectureId(employeeNumber, lectureId);
        if (lectureRegs == null) {
            throw new CoreException(CoreErrorType.NOT_FOUND_DATA);
        }
        return lectureRegs;
    }

    private void isValidEmployeeNumber(Integer employeeNumber) {
        boolean validNumber = Integer.toString(employeeNumber).length() == 5;
        if (!validNumber) {
            throw new CoreException(CoreErrorType.NOT_VALID_EMPLOYEE_NUMBER);
        }
    }

    private Lecture findLecture(Long lectureId) {
        Lecture lecture = this.lectureRepository.findById(lectureId);
        if (lecture == null) {
            throw new CoreException(CoreErrorType.NOT_FOUND_DATA);
        }
        return lecture;
    }

    private boolean existsLectureRegs(int employeeNumber, Long lectureId) {
        return this.lectureRegsRepository.existsByEmployeeNumberAndLectureId(employeeNumber, lectureId);
    }

    private void checkIsExceeded(NewLectureRegs lectureRegs, Lecture lecture) {
        int appliedCount = this.lectureRegsRepository.countByLectureId(lectureRegs.lectureId());
        lecture.isExceeded(appliedCount);
    }
}
