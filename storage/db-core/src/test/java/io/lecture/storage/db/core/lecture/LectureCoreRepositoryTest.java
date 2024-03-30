package io.lecture.storage.db.core.lecture;

import io.lecture.domain.lecture.domain.Lecture;
import io.lecture.domain.lecture.domain.NewLecture;
import io.lecture.storage.db.core.lecture.entity.LectureEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ LectureCoreRepository.class, LectureQueryDslRepository.class })
class LectureCoreRepositoryTest {

    @Autowired LectureCoreRepository lectureCoreRepository;

    @Autowired LectureJpaRepository lectureJpaRepository;

    @Autowired LectureRegsJpaRepository lectureRegsJpaRepository;

    @Autowired LectureQueryDslRepository lectureQueryDslRepository;

    @Test
    @DisplayName("강연 등록이 정상 동작한다")
    void append() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        NewLecture lecture = new NewLecture("김준우", "1강연장", 10, startAt, "스프링 강연");

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