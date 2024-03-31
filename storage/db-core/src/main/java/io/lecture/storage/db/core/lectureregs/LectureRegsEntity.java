package io.lecture.storage.db.core.lectureregs;

import io.lecture.domain.lecture.domain.LectureRegs;
import io.lecture.storage.db.core.BaseEntity;
import jakarta.persistence.*;

@Entity(name = "lecture_regs")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_number", "lecture_id"})})
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
        return new LectureRegs(super.getId(), this.employeeNumber, this.lectureId);
    }

}
