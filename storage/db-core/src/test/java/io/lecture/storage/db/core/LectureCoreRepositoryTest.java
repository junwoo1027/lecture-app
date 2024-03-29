package io.lecture.storage.db.core;

import io.lecture.domain.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(LectureCoreRepository.class)
class LectureCoreRepositoryTest {

    @Autowired
    LectureCoreRepository lectureCoreRepository;

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @Test
    @DisplayName("강연 등록이 정상 동작한다")
    void append() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        Lecture lecture = new Lecture("김준우", "1강연장", 10, startAt, "스프링 강연");

        // when
        Long successId = lectureCoreRepository.append(lecture);

        // then
        LectureEntity result = lectureJpaRepository.findById(successId).orElse(null);
        assertThat(result).isNotNull();
        assertThat(result.getLecturer()).isEqualTo("김준우");
        assertThat(result.getHall()).isEqualTo("1강연장");
        assertThat(result.getSeats()).isEqualTo(10);
        assertThat(result.getStartAt()).isEqualTo(startAt);
        assertThat(result.getDescription()).isEqualTo("스프링 강연");
    }

    @Test
    @DisplayName("시작 시간 1주일 전부터 시작 시간 1일 후 까지 강연의 목록을 조회한다.")
    void find() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        LocalDateTime start = now.minusWeeks(1);
        LocalDateTime end = now.plusDays(1);
        lectureJpaRepository.save(new LectureEntity("김준우", "1강연장", 10, start.plusDays(2), "스프링 강연"));
        lectureJpaRepository.save(new LectureEntity("준우1", "2강연장", 20, start.plusDays(3), "JPA 강연"));
        lectureJpaRepository.save(new LectureEntity("준우2", "3강연장", 25, start.minusDays(1), "DB 강연"));
        lectureJpaRepository.save(new LectureEntity("준우3", "4강연장", 25, start.plusDays(9), "JAVA 강연"));

        // when
        List<Lecture> results = lectureCoreRepository.find(start, end);

        // then
        assertThat(results).hasSize(2);
        assertThat(results).extracting("lecturer").containsExactlyInAnyOrder("김준우", "준우1");
        assertThat(results).extracting("hall").containsExactlyInAnyOrder("1강연장", "2강연장");
        assertThat(results).extracting("seats").containsExactlyInAnyOrder(10, 20);
        assertThat(results).extracting("startAt").containsExactlyInAnyOrder(start.plusDays(2), start.plusDays(3));
        assertThat(results).extracting("description").containsExactlyInAnyOrder("스프링 강연", "JPA 강연");
    }

    @Test
    @DisplayName("전체 강연 목록 조회가 정상 동작한다")
    void findAll() {
        // given
        LocalDateTime startAt1 = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        LocalDateTime startAt2 = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        lectureJpaRepository.save(new LectureEntity("김준우", "1강연장", 10, startAt1, "스프링 강연"));
        lectureJpaRepository.save(new LectureEntity("준우", "2강연장", 20, startAt2, "JPA 강연"));

        // when
        List<Lecture> results = lectureCoreRepository.findAll();

        // then
        assertThat(results).hasSize(2);
        assertThat(results).extracting("lecturer").containsExactlyInAnyOrder("김준우", "준우");
        assertThat(results).extracting("hall").containsExactlyInAnyOrder("1강연장", "2강연장");
        assertThat(results).extracting("seats").containsExactlyInAnyOrder(10, 20);
        assertThat(results).extracting("startAt").containsExactlyInAnyOrder(startAt1, startAt2);
        assertThat(results).extracting("description").containsExactlyInAnyOrder("스프링 강연", "JPA 강연");
    }
}