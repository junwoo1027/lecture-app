package io.lecture.storage.db.core.lecture;

import io.lecture.storage.db.core.lecture.entity.LectureEntity;
import io.lecture.storage.db.core.lecture.entity.LectureRegsEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ LectureQueryDslRepository.class })
class LectureQueryDslRepositoryTest {

    @Autowired LectureJpaRepository lectureJpaRepository;

    @Autowired LectureRegsJpaRepository lectureRegsJpaRepository;

    @Autowired LectureQueryDslRepository lectureQueryDslRepository;

    @Test
    @DisplayName("신청한 강연 목록 조회가 정상 동작한다")
    public void testFindLecturesByEmployee() {
        // given
        final int employeeNumber = 12345;
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 1, 0, 0, 0);
        for (int i = 1;  i <= 5; i++) {
            lectureJpaRepository.save(new LectureEntity("준우", i + "강연장", i, startAt, "JPA 강연"));
        }
        lectureRegsJpaRepository.save(new LectureRegsEntity(employeeNumber, 3L));
        lectureRegsJpaRepository.save(new LectureRegsEntity(employeeNumber, 4L));
        lectureRegsJpaRepository.save(new LectureRegsEntity(employeeNumber, 5L));

        List<LectureEntity> all = lectureJpaRepository.findAll();

        for (LectureEntity lectureEntity : all) {
            System.out.println(lectureEntity.getHall());
        }

        System.out.println(employeeNumber);

        List<LectureRegsEntity> all1 = lectureRegsJpaRepository.findAll();
        for (LectureRegsEntity lectureRegsEntity : all1) {
            System.out.println(lectureRegsEntity.getEmployeeNumber());
            System.out.println(lectureRegsEntity.getLectureId());
        }


        // when
        List<LectureEntity> result = lectureQueryDslRepository.findLecturesByEmployee(employeeNumber);

        for (LectureEntity lectureEntity : result) {
            System.out.println(lectureEntity.getHall());
        }

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting("lecturer").containsOnly("준우");
        assertThat(result).extracting("hall").containsExactly("3강연장", "4강연장", "5강연장");
        assertThat(result).extracting("seats").containsExactly(3, 4, 5);
        assertThat(result).extracting("startAt").containsOnly(startAt);
        assertThat(result).extracting("description").containsOnly("JPA 강연");
    }

//    @Test
//    @DisplayName("최근 3일간 신청이 많은 강연 목록 조회가 정상 작동한다")
//    void findPopularLectures() throws NoSuchFieldException, IllegalAccessException {
//        // given
//        LocalDateTime now = LocalDateTime.now();
//
//        for (int i = 1;  i <= 3; i++) {
//            lectureJpaRepository.save(new LectureEntity("준우", i + "강연장", i, now, "JPA 강연"));
//        }
//
//        LocalDateTime oneDaysAgo = now.minusDays(1);
//        LocalDateTime twoDaysAgo = now.minusDays(2);
//        LocalDateTime threeDaysAgo = now.minusDays(3);
//        LocalDateTime fourDaysAgo = now.minusDays(4);
//
//        final int employeeNumber1 = 111111;
//        final int employeeNumber2 = 22222;
//        final int employeeNumber3 = 33333;
//        final int employeeNumber4 = 44444;
//
//        setCreatedAtForRegistration(employeeNumber1, 1L, oneDaysAgo);
//        setCreatedAtForRegistration(employeeNumber2, 1L, oneDaysAgo);
//        setCreatedAtForRegistration(employeeNumber3, 1L, oneDaysAgo);
//
//        setCreatedAtForRegistration(employeeNumber1, 2L, twoDaysAgo);
//
//        setCreatedAtForRegistration(employeeNumber1, 3L, threeDaysAgo);
//        setCreatedAtForRegistration(employeeNumber2, 3L, threeDaysAgo);
//        setCreatedAtForRegistration(employeeNumber3, 3L, fourDaysAgo);
//        setCreatedAtForRegistration(employeeNumber4, 3L, fourDaysAgo);
//
//        // 1L 3개
//        // 3L 2개
//        // 2L 1개
//
//        // when
//        List<LectureEntity> result = lectureQueryDslRepository.findPopularLectures(threeDaysAgo);
//
//        // then
//        assertThat(result).hasSize(3);
//        assertThat(result).extracting("lecturer").containsOnly("준우");
//        assertThat(result).extracting("hall").containsExactly("1강연장", "3강연장", "2강연장");
//        assertThat(result).extracting("seats").containsExactly(1, 3, 2);
//        assertThat(result).extracting("startAt").containsOnly(now);
//        assertThat(result).extracting("description").containsOnly("JPA 강연");
//    }

    private void setCreatedAtForRegistration(int employeeNumber, long lectureId, LocalDateTime createdAt) throws NoSuchFieldException, IllegalAccessException {
        LectureRegsEntity lectureRegs = lectureRegsJpaRepository.save(new LectureRegsEntity(employeeNumber, lectureId));
        setCreatedAt(lectureRegs, createdAt);
    }

    private Field findFieldRecursively(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return findFieldRecursively(superClass, fieldName);
            }
        }
    }

    private void setCreatedAt(Object object, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = findFieldRecursively(object.getClass(), "createdAt");
        field.setAccessible(true);
        field.set(object, value);
    }
}