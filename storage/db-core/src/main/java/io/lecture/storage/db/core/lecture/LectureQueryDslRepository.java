package io.lecture.storage.db.core.lecture;

import com.querydsl.jpa.impl.JPAQueryFactory;

import io.lecture.storage.db.core.lectureregs.QLectureRegsEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LectureQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LectureQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<LectureEntity> findLecturesByEmployee(int employeeNumber) {
        QLectureEntity qLecture = QLectureEntity.lectureEntity;
        QLectureRegsEntity qLectureRegs = QLectureRegsEntity.lectureRegsEntity;

        return queryFactory.select(qLecture)
            .from(qLecture)
            .innerJoin(qLectureRegs)
            .on(qLecture.id.eq(qLectureRegs.lectureId))
            .where(qLectureRegs.employeeNumber.eq(employeeNumber))
            .fetch();
    }

    public List<LectureEntity> findPopularLectures(LocalDateTime datetime) {
        QLectureEntity qLecture = QLectureEntity.lectureEntity;
        QLectureRegsEntity qLectureRegs = QLectureRegsEntity.lectureRegsEntity;

        return queryFactory.select(qLecture)
            .from(qLectureRegs)
            .innerJoin(qLecture)
            .on(qLecture.id.eq(qLectureRegs.lectureId))
            .where(qLectureRegs.createdAt.goe(datetime))
            .groupBy(qLecture.id)
            .orderBy(qLectureRegs.lectureId.count().desc())
            .fetch();
    }

}
