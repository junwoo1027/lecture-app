package io.lecture.core.api.domain;

import io.lecture.core.api.controller.v1.request.NewLectureRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LectureServiceTest {

    @Autowired
    LectureService lectureService;

    @Autowired
    LectureRepository lectureRepository;

    @AfterEach
    void clean() {
        lectureRepository.deleteAll();
    }

    @Test
    @DisplayName("강연 등록이 정상 동작한다")
    void append() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        NewLectureRequest request = new NewLectureRequest("김준우", "1강연장", 10, startAt, "스프링 강연");

        // when
        lectureService.append(request.toLecture());

        // then
        List<Lecture> results = lectureRepository.findAll();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).lecturer()).isEqualTo("김준우");
        assertThat(results.get(0).hall()).isEqualTo("1강연장");
        assertThat(results.get(0).seats()).isEqualTo(10);
        assertThat(results.get(0).startAt()).isEqualTo(startAt);
        assertThat(results.get(0).description()).isEqualTo("스프링 강연");
    }
}
