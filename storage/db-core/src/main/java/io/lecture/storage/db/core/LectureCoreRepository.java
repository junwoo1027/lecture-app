package io.lecture.storage.db.core;

import io.lecture.domain.Lecture;
import io.lecture.domain.LectureRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LectureCoreRepository implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;

    public LectureCoreRepository(LectureJpaRepository lectureJpaRepository) {
        this.lectureJpaRepository = lectureJpaRepository;
    }

    @Override
    public Long append(Lecture lecture) {
        return lectureJpaRepository.save(
                new LectureEntity(lecture.lecturer(), lecture.hall(), lecture.seats(), lecture.startAt(), lecture.description())
        ).getId();
    }

    @Override
    public List<Lecture> find() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekBefore = now.minusWeeks(1);
        LocalDateTime dayAfter = now.plusDays(1);

        return lectureJpaRepository.findByStartAtBetweenOrderByStartAt(weekBefore, dayAfter)
                .stream().map(LectureEntity::toLecture).collect(Collectors.toList());
    }

    @Override
    public List<Lecture> findAll() {
        return lectureJpaRepository.findAll().stream().map(LectureEntity::toLecture).collect(Collectors.toList());
    }
}
