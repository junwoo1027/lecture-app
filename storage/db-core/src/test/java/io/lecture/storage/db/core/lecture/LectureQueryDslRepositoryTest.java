package io.lecture.storage.db.core.lecture;

import io.lecture.storage.db.core.lectureregs.LectureRegsEntity;
import io.lecture.storage.db.core.lectureregs.LectureRegsJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ LectureQueryDslRepository.class })
class LectureQueryDslRepositoryTest {

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @Autowired
    LectureRegsJpaRepository lectureRegsJpaRepository;

    @Autowired
    LectureQueryDslRepository lectureQueryDslRepository;

    @Test
    @DisplayName("신청한 강연 목록 조회가 정상 동작한다")
    public void testFindLecturesByEmployee() {
        // given
        final int employeeNumber = 12345;
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 1, 0, 0, 0);
        for (int i = 1; i <= 5; i++) {
            lectureJpaRepository.save(new LectureEntity("준우", i + "강연장", i, startAt, "JPA 강연"));
        }
        lectureRegsJpaRepository.save(new LectureRegsEntity(employeeNumber, 3L));
        lectureRegsJpaRepository.save(new LectureRegsEntity(employeeNumber, 4L));
        lectureRegsJpaRepository.save(new LectureRegsEntity(employeeNumber, 5L));

        // when
        List<LectureEntity> result = lectureQueryDslRepository.findLecturesByEmployee(employeeNumber);

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting("lecturer").containsOnly("준우");
        assertThat(result).extracting("hall").containsExactly("3강연장", "4강연장", "5강연장");
        assertThat(result).extracting("seats").containsExactly(3, 4, 5);
        assertThat(result).extracting("startAt").containsOnly(startAt);
        assertThat(result).extracting("description").containsOnly("JPA 강연");
    }

}