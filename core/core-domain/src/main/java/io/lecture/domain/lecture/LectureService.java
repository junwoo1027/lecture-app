package io.lecture.domain.lecture;

import io.lecture.domain.error.CoreErrorType;
import io.lecture.domain.error.CoreException;

import io.lecture.domain.lecture.domain.*;
import io.lecture.redisson.aop.RedissonLock;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    private final LectureRegsRepository lectureRegsRepository;

    public LectureService(LectureRepository lectureRepository, LectureRegsRepository lectureRegsRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureRegsRepository = lectureRegsRepository;
    }

    /** 강연 등록 */
    public Long append(NewLecture lecture) {
        return this.lectureRepository.append(lecture);
    }

    /** 전체 강연 목록 조회 */
    public List<Lecture> findLectures() {
        return this.lectureRepository.findAll();
    }

    /** 강연 신청 */
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

    /** 신청 강연 취소 */
    public void cancel(CancelLectureRegs cancelLectureRegs) {
        LectureRegs lectureRegs = findLectureRegistrationsByLecture(cancelLectureRegs.employeeNumber(),
                cancelLectureRegs.lectureId());
        this.lectureRegsRepository.cancel(lectureRegs.id());
    }

    /** 강연 신청자 목록 조회 */
    public List<LectureRegs> findLectureRegistrationsByLecture(Long lectureId) {
        return this.lectureRegsRepository.getLectureRegsListByLecture(lectureId);
    }

    /** 신청한 강연 목록 조회 */
    public List<Lecture> findLecturesByEmployee(int employeeNumber) {
        return this.lectureRepository.findLecturesByEmployee(employeeNumber);
    }

    /** 실시간 인기 강연 목록 조회 */
    public List<Lecture> findPopularLectures() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        return this.lectureRepository.findPopularLectures(threeDaysAgo);
    }

    private LectureRegs findLectureRegistrationsByLecture(int employeeNumber, Long lectureId) {
        LectureRegs lectureRegs = this.lectureRegsRepository.findLectureRegsByEmployeeNumberAndLectureId(employeeNumber,
                lectureId);
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
