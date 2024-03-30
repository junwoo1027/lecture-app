package io.lecture.storage.db.core;

import io.lecture.domain.LectureRegs;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "lecture_regs")
public class LectureRegsEntity extends BaseEntity {
    @Column(name = "employee_number")
    private int employeeNumber;
    @Column(name = "lecture_id")
    private Long lectureId;

    public LectureRegsEntity() {
    }

    public LectureRegsEntity(int employeeNumber, Long lectureId) {
        this.employeeNumber = employeeNumber;
        this.lectureId = lectureId;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public LectureRegs toLectureRegs() {
        return new LectureRegs(
                this.employeeNumber,
                this.lectureId
        );
    }
}
