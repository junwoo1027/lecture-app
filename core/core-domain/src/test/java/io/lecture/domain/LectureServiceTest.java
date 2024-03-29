package io.lecture.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    private LectureService lectureService;

    @Test
    @DisplayName("강연 등록이 정상 동작한다")
    void append() {
        // given
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        Lecture lecture = new Lecture("김준우", "1강연장", 10, startAt, "스프링 강연");

        when(lectureRepository.append(any(Lecture.class))).thenReturn(1L);

        // when
        Long successId = lectureService.append(lecture);

        // then
        assertThat(successId).isEqualTo(1L);
        verify(lectureRepository, times(1)).append(lecture);
    }

    @Test
    @DisplayName("시작 시간 1주일 전부터 시작 시간 1일 후 까지 강연의 목록을 조회한다.")
    void find() {
        LocalDateTime now = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            // given
            LocalDateTime fixedDateTime = now;
            LocalDateTime start = now.minusWeeks(1);
            LocalDateTime end = now.plusDays(1);
            List<Lecture> lectures = List.of(
                    new Lecture("김준우", "1강연장", 10, start.plusDays(2), "스프링 강연"),
                    new Lecture("준우1", "2강연장", 20, start.plusDays(3), "JPA 강연"),
                    new Lecture("준우2", "3강연장", 25, start.minusDays(1), "DB 강연"),
                    new Lecture("준우3", "4강연장", 25, start.plusDays(9), "JAVA 강연")
            );
            List<Lecture> activeLectures = lectures.stream()
                    .filter(l -> !l.startAt().isBefore(start) && !l.startAt().isAfter(end))
                    .collect(Collectors.toList());

            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDateTime);
            when(lectureRepository.find(start, end)).thenReturn(activeLectures);

            // when
            List<Lecture> results = lectureService.find();

            // then
            assertThat(results).hasSize(2);
            assertThat(results).extracting("lecturer").containsExactlyInAnyOrder("김준우", "준우1");
            assertThat(results).extracting("hall").containsExactlyInAnyOrder("1강연장", "2강연장");
            assertThat(results).extracting("seats").containsExactlyInAnyOrder(10, 20);
            assertThat(results).extracting("startAt").containsExactlyInAnyOrder(start.plusDays(2), start.plusDays(3));
            assertThat(results).extracting("description").containsExactlyInAnyOrder("스프링 강연", "JPA 강연");
            verify(lectureRepository, times(1)).find(start, end);
        }
    }

    @Test
    @DisplayName("전체 강연 목록 조회가 정상 동작한다")
    void findAll() {
        // given
        LocalDateTime startAt1 = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        LocalDateTime startAt2 = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        List<Lecture> lectures = List.of(
                new Lecture("김준우", "1강연장", 10, startAt1, "스프링 강연"),
                new Lecture("준우", "2강연장", 20, startAt2, "JPA 강연")
        );

        when(lectureRepository.findAll()).thenReturn(lectures);

        // when
        List<Lecture> results = lectureService.findAll();

        // then
        assertThat(results).hasSize(2);
        assertThat(results).extracting("lecturer").containsExactlyInAnyOrder("김준우", "준우");
        assertThat(results).extracting("hall").containsExactlyInAnyOrder("1강연장", "2강연장");
        assertThat(results).extracting("seats").containsExactlyInAnyOrder(10, 20);
        assertThat(results).extracting("startAt").containsExactlyInAnyOrder(startAt1, startAt2);
        assertThat(results).extracting("description").containsExactlyInAnyOrder("스프링 강연", "JPA 강연");
        verify(lectureRepository, times(1)).findAll();
    }
}