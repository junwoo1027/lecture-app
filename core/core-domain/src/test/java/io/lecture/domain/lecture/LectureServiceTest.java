package io.lecture.domain.lecture;

import io.lecture.domain.error.CoreErrorCode;
import io.lecture.domain.error.CoreException;
import io.lecture.domain.lecture.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LectureRegsRepository lectureRegsRepository;

    @InjectMocks
    private LectureService lectureService;

    @Test
    @DisplayName("강연 등록이 정상 동작한다")
    void append() {
        // given
        LocalDateTime startAt1 = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        LocalDateTime startAt2 = LocalDateTime.of(2024, 3, 28, 0, 0, 0);
        NewLecture lecture1 = new NewLecture("김준우", "1강연장", 10, startAt1, "스프링 강연");
        NewLecture lecture2 = new NewLecture("김준우", "2강연장", 5, startAt2, "JPA 강연");

        when(lectureRepository.append(lecture1)).thenReturn(1L);
        when(lectureRepository.append(lecture2)).thenReturn(2L);

        // when
        Long successId1 = lectureService.append(lecture1);
        Long successId2 = lectureService.append(lecture2);

        // then
        assertThat(successId1).isEqualTo(1L);
        assertThat(successId2).isEqualTo(2L);
        verify(lectureRepository, times(1)).append(lecture1);
        verify(lectureRepository, times(1)).append(lecture2);
    }

    @Test
    @DisplayName("전체 강연 목록 조회가 정상 동작한다")
    void findAll() {
        // given
        LocalDateTime startAt1 = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        LocalDateTime startAt2 = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        List<Lecture> lectures = List.of(new Lecture(1L, "김준우", "1강연장", 10, startAt1, "스프링 강연"),
                new Lecture(2L, "준우", "2강연장", 20, startAt2, "JPA 강연"));

        when(lectureRepository.findAll()).thenReturn(lectures);

        // when
        List<Lecture> results = lectureService.findLectures();

        // then
        assertThat(results).hasSize(2);
        assertThat(results).extracting("lecturer").containsExactlyInAnyOrder("김준우", "준우");
        assertThat(results).extracting("hall").containsExactlyInAnyOrder("1강연장", "2강연장");
        assertThat(results).extracting("seats").containsExactlyInAnyOrder(10, 20);
        assertThat(results).extracting("startAt").containsExactlyInAnyOrder(startAt1, startAt2);
        assertThat(results).extracting("description").containsExactlyInAnyOrder("스프링 강연", "JPA 강연");
        verify(lectureRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("강연 신청이 정상 동작한다")
    void apply() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        NewLectureRegs lectureRegs = new NewLectureRegs(employeeNumber, lectureId);
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        Lecture lecture = new Lecture(1L, "김준우", "1강연장", 5, startAt, "스프링 강연");

        when(lectureRepository.findById(lectureId)).thenReturn(lecture);
        when(lectureRegsRepository.existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(),
                lectureRegs.lectureId()))
            .thenReturn(false);
        when(lectureRegsRepository.countByLectureId(lectureId)).thenReturn(3);
        when(lectureRegsRepository.apply(lectureRegs)).thenReturn(1L);

        // when
        Long successId = lectureService.apply("", lectureRegs);

        // then
        assertThat(successId).isEqualTo(1L);
        verify(lectureRepository, times(1)).findById(lectureId);
        verify(lectureRegsRepository, times(1)).existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(),
                lectureRegs.lectureId());
        verify(lectureRegsRepository, times(1)).countByLectureId(lectureId);
        verify(lectureRegsRepository, times(1)).apply(lectureRegs);
    }

    @Test
    @DisplayName("존재하지 않은 강연을 신청하면 실패한다")
    void notFoundLectureApplyError() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        NewLectureRegs lectureRegs = new NewLectureRegs(employeeNumber, lectureId);

        when(lectureRepository.findById(lectureId)).thenReturn(null);

        // when && then
        CoreException thrown = assertThrows(CoreException.class, () -> lectureService.apply("", lectureRegs));
        assertThat(thrown.getErrorType().getCode()).isEqualTo(CoreErrorCode.E1000);
        assertThat(thrown.getErrorType().getMessage()).isEqualTo("Not found data.");
    }

    @Test
    @DisplayName("이미 해당 강연을 신청했으면 실패한다")
    void alreadyApplyError() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        NewLectureRegs lectureRegs = new NewLectureRegs(employeeNumber, lectureId);
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        Lecture lecture = new Lecture(1L, "김준우", "1강연장", 5, startAt, "스프링 강연");

        when(lectureRepository.findById(lectureId)).thenReturn(lecture);
        when(lectureRegsRepository.existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(),
                lectureRegs.lectureId()))
            .thenReturn(true);

        // when && then
        CoreException thrown = assertThrows(CoreException.class, () -> lectureService.apply("", lectureRegs));
        assertThat(thrown.getErrorType().getCode()).isEqualTo(CoreErrorCode.E1002);
        assertThat(thrown.getErrorType().getMessage()).isEqualTo("Already applied for a lecture.");
        verify(lectureRepository, times(1)).findById(lectureId);
        verify(lectureRegsRepository, times(1)).existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(),
                lectureRegs.lectureId());
    }

    @Test
    @DisplayName("강연 참석 인원이 정원에 도달하면 실패한다")
    void exceededLectureApplyError() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        NewLectureRegs lectureRegs = new NewLectureRegs(employeeNumber, lectureId);
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        Lecture lecture = new Lecture(1L, "김준우", "1강연장", 5, startAt, "스프링 강연");

        when(lectureRepository.findById(lectureId)).thenReturn(lecture);
        when(lectureRegsRepository.existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(),
                lectureRegs.lectureId()))
            .thenReturn(false);
        when(lectureRegsRepository.countByLectureId(lectureId)).thenReturn(5);

        // when && then
        CoreException thrown = assertThrows(CoreException.class, () -> lectureService.apply("", lectureRegs));
        assertThat(thrown.getErrorType().getCode()).isEqualTo(CoreErrorCode.E1001);
        assertThat(thrown.getErrorType().getMessage()).isEqualTo("Lecture has been exceeded.");
        verify(lectureRepository, times(1)).findById(lectureId);
        verify(lectureRegsRepository, times(1)).existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(),
                lectureRegs.lectureId());
        verify(lectureRegsRepository, times(1)).countByLectureId(lectureId);
    }

    @Test
    @DisplayName("강연 신청 취소가 정상 동작한다")
    void cancel() {
        // given
        int employeeNumber = 12345;
        long lectureId = 1L;
        CancelLectureRegs cancelLectureRegs = new CancelLectureRegs(employeeNumber, lectureId);
        LectureRegs lectureRegs = new LectureRegs(2L, employeeNumber, cancelLectureRegs.lectureId());

        when(lectureRegsRepository.findLectureRegsByEmployeeNumberAndLectureId(cancelLectureRegs.employeeNumber(),
                cancelLectureRegs.lectureId()))
            .thenReturn(lectureRegs);

        // when
        lectureService.cancel(cancelLectureRegs);

        // then
        verify(lectureRegsRepository, times(1)).cancel(lectureRegs.id());
        verify(lectureRegsRepository, times(1)).findLectureRegsByEmployeeNumberAndLectureId(
                cancelLectureRegs.employeeNumber(), cancelLectureRegs.lectureId());
    }

    @Test
    @DisplayName("강연 신청 취소 시 신청 내역이 없으면 실패한다")
    void cancelFail() {
        // given
        int employeeNumber = 12345;
        long lectureId = 1L;
        CancelLectureRegs cancelLectureRegs = new CancelLectureRegs(employeeNumber, lectureId);

        when(lectureRegsRepository.findLectureRegsByEmployeeNumberAndLectureId(cancelLectureRegs.employeeNumber(),
                cancelLectureRegs.lectureId()))
            .thenReturn(null);

        // when && then
        CoreException thrown = assertThrows(CoreException.class, () -> lectureService.cancel(cancelLectureRegs));
        assertThat(thrown.getErrorType().getCode()).isEqualTo(CoreErrorCode.E1000);
        assertThat(thrown.getErrorType().getMessage()).isEqualTo("Not found data.");
        verify(lectureRegsRepository, times(1)).findLectureRegsByEmployeeNumberAndLectureId(
                cancelLectureRegs.employeeNumber(), cancelLectureRegs.lectureId());
    }

    @Test
    @DisplayName("강연 신청자 목록 조회가 정상 동작한다")
    void findLectureRegsList() {
        // given
        Long lectureId = 1L;
        List<LectureRegs> allLectureRegs = List.of(new LectureRegs(1L, 11111, 1L), new LectureRegs(2L, 22222, 1L),
                new LectureRegs(3L, 33333, 2L));
        List<LectureRegs> expectedList = allLectureRegs.stream()
            .filter(each -> each.lectureId() == lectureId)
            .collect(Collectors.toList());

        when(lectureRegsRepository.getLectureRegsListByLecture(lectureId)).thenReturn(expectedList);

        // when
        List<LectureRegs> results = lectureService.findLectureRegistrationsByLecture(lectureId);

        // then
        assertThat(results).hasSize(2);
        assertThat(results).extracting("employeeNumber").containsExactlyInAnyOrder(11111, 22222);
        assertThat(results).extracting("lectureId").containsExactlyInAnyOrder(1L, 1L);
        verify(lectureRegsRepository, times(1)).getLectureRegsListByLecture(lectureId);
    }

    @Test
    @DisplayName("신청한 강연 목록 조회가 정상 동작한다")
    void findLecturesByEmployee() {
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        List<Lecture> lectures = List.of(new Lecture(1L, "김준우", "1강연장", 10, startAt, "스프링 강연"),
                new Lecture(2L, "김준우", "2강연장", 11, startAt, "JPA 강연"),
                new Lecture(3L, "김준우", "3강연장", 12, startAt, "자바 강연"));

        int employeeNumber1 = 11111;
        List<LectureRegs> allLectureRegs = List.of(new LectureRegs(1L, employeeNumber1, lectures.get(0).id()),
                new LectureRegs(2L, employeeNumber1, lectures.get(1).id()),
                new LectureRegs(3L, 22222, lectures.get(0).id()), new LectureRegs(4L, 22222, lectures.get(1).id()),
                new LectureRegs(5L, 22222, lectures.get(2).id()));

        // Create a map of lectures for quick lookup by ID
        Map<Long, Lecture> lectureMap = lectures.stream().collect(Collectors.toMap(Lecture::id, lecture -> lecture));

        // Filter and collect matching lectures for a specific employee
        List<Lecture> expectedList = allLectureRegs.stream()
            .filter(reg -> reg.employeeNumber() == employeeNumber1)
            .map(reg -> lectureMap.get(reg.lectureId()))
            .collect(Collectors.toList());

        when(lectureRepository.findLecturesByEmployee(employeeNumber1)).thenReturn(expectedList);

        // when
        List<Lecture> results = lectureService.findLecturesByEmployee(employeeNumber1);

        // then
        assertThat(results).hasSize(2);
        assertThat(results).extracting("id").containsExactlyInAnyOrder(1L, 2L);
        assertThat(results).extracting("lecturer").containsOnly("김준우");
        assertThat(results).extracting("hall").containsExactlyInAnyOrder("1강연장", "2강연장");
        assertThat(results).extracting("seats").containsExactlyInAnyOrder(10, 11);
        assertThat(results).extracting("startAt").containsOnly(startAt);
        assertThat(results).extracting("description").containsExactlyInAnyOrder("스프링 강연", "JPA 강연");
        verify(lectureRepository, times(1)).findLecturesByEmployee(employeeNumber1);
    }

    @Test
    @DisplayName("신최근 3일간 신청이 많은 강연 목록 조회가 정상 작동한다")
    void findPopularLectures() {
        LocalDateTime now = LocalDateTime.now();
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime fixedDateTime = now;

            LocalDateTime threeDaysAgo = now.minusDays(3);
            int employeeNumber1 = 11111;
            List<LectureRegs> allLectureRegs = List.of(new LectureRegs(1L, employeeNumber1, 2L),
                    new LectureRegs(2L, employeeNumber1, 2L), new LectureRegs(3L, 22222, 1L),
                    new LectureRegs(4L, 22222, 2L), new LectureRegs(5L, 22222, 3L), new LectureRegs(6L, 33333, 1L));

            // 강의 ID별 등록 횟수를 카운트
            Map<Long, Long> lectureCount = allLectureRegs.stream()
                .collect(Collectors.groupingBy(LectureRegs::lectureId, Collectors.counting()));

            // 카운트 기반으로 내림차순 정렬
            List<Map.Entry<Long, Long>> sortedLectures = new ArrayList<>(lectureCount.entrySet());
            sortedLectures.sort(Map.Entry.<Long, Long>comparingByValue().reversed());

            List<Lecture> expectedList = sortedLectures.stream()
                .map(each -> new Lecture(each.getKey(), "test", "test", 2, fixedDateTime, "test"))
                .collect(Collectors.toList());

            // 2L 3개
            // 1L 2개
            // 3L 1개

            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(fixedDateTime);
            when(lectureRepository.findPopularLectures(threeDaysAgo)).thenReturn(expectedList);

            // when
            List<Lecture> results = lectureService.findPopularLectures();

            // then
            assertThat(results).hasSize(3);
            assertThat(results).extracting("id").containsExactly(2L, 1L, 3L);
            verify(lectureRepository, times(1)).findPopularLectures(threeDaysAgo);
        }
    }

}