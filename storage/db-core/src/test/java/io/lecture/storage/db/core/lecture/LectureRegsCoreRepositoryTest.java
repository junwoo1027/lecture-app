package io.lecture.storage.db.core.lecture;

import io.lecture.domain.lecture.domain.LectureRegs;
import io.lecture.domain.lecture.NewLectureRegs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(LectureRegsCoreRepository.class)
class LectureRegsCoreRepositoryTest {

    @Autowired LectureRegsCoreRepository lectureRegsCoreRepository;

    @Autowired LectureRegsJpaRepository lectureRegsJpaRepository;

    @Autowired LectureJpaRepository lectureJpaRepository;

    @BeforeEach
    void setup() {
        saveLecture();
    }

    @Test
    @DisplayName("강연 등록이 정상 동작한다")
    void apply() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        NewLectureRegs lectureRegs = new NewLectureRegs(employeeNumber, lectureId);

        // when
        Long successId = lectureRegsCoreRepository.apply(lectureRegs);

        // then
        LectureRegsEntity result = lectureRegsJpaRepository.findById(successId).orElse(null);
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeNumber()).isEqualTo(employeeNumber);
        assertThat(result.getLectureId()).isEqualTo(lectureId);
    }

    @Test
    @DisplayName("이미 신청한 강연이면 true 를 반환한다")
    void existsByEmployeeNumberAndLectureIdIsTrue() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        LectureRegsEntity lectureRegsEntity = new LectureRegsEntity(employeeNumber, lectureId);
        lectureRegsJpaRepository.save(lectureRegsEntity);

        // when
        boolean result = lectureRegsCoreRepository.existsByEmployeeNumberAndLectureId(employeeNumber, lectureId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("신청하지 않은 강연이면 false 를 반환한다")
    void existsByEmployeeNumberAndLectureIdIsFalse() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;

        // when
        boolean result = lectureRegsCoreRepository.existsByEmployeeNumberAndLectureId(employeeNumber, lectureId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("강연에 신청된 개수 조회가 정상 동작한다")
    void countByLectureId() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        LectureRegsEntity lectureRegsEntity = new LectureRegsEntity(employeeNumber, lectureId);
        lectureRegsJpaRepository.save(lectureRegsEntity);

        // when
        int result = lectureRegsCoreRepository.countByLectureId(lectureId);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("강연신청 정보 조회가 정상 동작한다")
    void findLectureRegsByEmployeeNumberAndLectureId() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        LectureRegsEntity lectureRegsEntity = new LectureRegsEntity(employeeNumber, lectureId);
        lectureRegsJpaRepository.save(lectureRegsEntity);

        // when
        LectureRegs result = this.lectureRegsCoreRepository.findLectureRegsByEmployeeNumberAndLectureId(employeeNumber, lectureId);

        // then
        assertThat(result.employeeNumber()).isEqualTo(employeeNumber);
        assertThat(result.lectureId()).isEqualTo(lectureId);
    }

    @Test
    @DisplayName("신청한 강연의 취소가 정상 동작한다")
    void cancel() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        LectureRegsEntity lectureRegsEntity = new LectureRegsEntity(employeeNumber, lectureId);
        lectureRegsJpaRepository.save(lectureRegsEntity);

        // when
        this.lectureRegsCoreRepository.cancel(lectureRegsEntity.getId());

        // then
        List<LectureRegsEntity> result = lectureRegsJpaRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("강연 신청자 목록 조회가 정상 동작한다")
    void findAllByLectureId() {
        // given
        Long lectureId = 1L;
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        LectureEntity lectureEntity = new LectureEntity("김준우", "2강연장", 5, startAt, "자바 강연");
        lectureJpaRepository.save(lectureEntity);

        lectureRegsJpaRepository.save(new LectureRegsEntity(11111, 1L));
        lectureRegsJpaRepository.save(new LectureRegsEntity(22222, 1L));
        lectureRegsJpaRepository.save(new LectureRegsEntity(33333, 1L));
        lectureRegsJpaRepository.save(new LectureRegsEntity(44444, 2L));

        // when
        List<LectureRegsEntity> result = lectureRegsJpaRepository.findAllByLectureId(lectureId);

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting("employeeNumber").containsExactlyInAnyOrder(11111, 22222, 33333);
        assertThat(result).extracting("lectureId").containsExactlyInAnyOrder(1L, 1L, 1L);
    }

    private void saveLecture() {
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        LectureEntity lectureEntity = new LectureEntity("김준우", "1강연장", 5, startAt, "스프링 강연");
        lectureJpaRepository.save(lectureEntity);
    }
}
