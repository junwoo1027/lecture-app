package io.lecture.storage.db.core.lecture;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

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

        return queryFactory
                .select(qLecture)
                .from(qLecture)
                .join(qLectureRegs).on(qLecture.id.eq(qLectureRegs.lectureId))
                .where(qLectureRegs.employeeNumber.eq(employeeNumber))
                .fetch();
    }
}
