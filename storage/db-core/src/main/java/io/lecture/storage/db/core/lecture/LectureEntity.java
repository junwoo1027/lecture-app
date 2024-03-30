package io.lecture.storage.db.core.lecture;

import io.lecture.domain.lecture.Lecture;
import io.lecture.storage.db.core.BaseEntity;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity(name = "lecture")
public class LectureEntity extends BaseEntity {
    private String lecturer;
    private String hall;
    private int seats;
    private LocalDateTime startAt;
    private String description;

    public LectureEntity() {
    }

    public LectureEntity(String lecturer, String hall, int seats, LocalDateTime startAt, String description) {
        this.lecturer = lecturer;
        this.hall = hall;
        this.seats = seats;
        this.startAt = startAt;
        this.description = description;
    }

    public String getLecturer() {
        return lecturer;
    }

    public String getHall() {
        return hall;
    }

    public int getSeats() {
        return seats;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public String getDescription() {
        return description;
    }

    public Lecture toLecture() {
        return new Lecture(
                super.getId(),
                this.lecturer,
                this.hall,
                this.seats,
                this.startAt,
                this.description
        );
    }
}
