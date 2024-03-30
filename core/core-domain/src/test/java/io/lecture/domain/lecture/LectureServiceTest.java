package io.lecture.domain.lecture;

import io.lecture.domain.error.CoreErrorCode;
import io.lecture.domain.error.CoreException;
import io.lecture.domain.lecture.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

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
        Lecture lecture1 = new Lecture("김준우", "1강연장", 10, startAt1, "스프링 강연");
        Lecture lecture2 = new Lecture("김준우", "2강연장", 5, startAt2, "JPA 강연");

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

    @Test
    @DisplayName("강연 신청이 정상 동작한다")
    void apply() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        LectureRegs lectureRegs = new LectureRegs(employeeNumber, lectureId);
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        Lecture lecture = new Lecture("김준우", "1강연장", 5, startAt, "스프링 강연");

        when(lectureRepository.findById(lectureId)).thenReturn(lecture);
        when(lectureRegsRepository.existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(), lectureRegs.lectureId())).thenReturn(false);
        when(lectureRegsRepository.countByLectureId(lectureId)).thenReturn(3);
        when(lectureRegsRepository.apply(lectureRegs)).thenReturn(1L);

        // when
        Long successId = lectureService.apply("", lectureRegs);

        // then
        assertThat(successId).isEqualTo(1L);
        verify(lectureRepository, times(1)).findById(lectureId);
        verify(lectureRegsRepository, times(1)).existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(), lectureRegs.lectureId());
        verify(lectureRegsRepository, times(1)).countByLectureId(lectureId);
        verify(lectureRegsRepository, times(1)).apply(lectureRegs);
    }

    @Test
    @DisplayName("존재하지 않은 강연을 신청하면 실패한다")
    void not_found_lecture_apply_error() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        LectureRegs lectureRegs = new LectureRegs(employeeNumber, lectureId);

        when(lectureRepository.findById(lectureId)).thenReturn(null);

        // when && then
        CoreException thrown = assertThrows(CoreException.class, () -> lectureService.apply("", lectureRegs));
        assertThat(thrown.getErrorType().getCode()).isEqualTo(CoreErrorCode.E1000);
        assertThat(thrown.getErrorType().getMessage()).isEqualTo("Not found data.");
    }

    @Test
    @DisplayName("이미 해당 강연을 신청했으면 실패한다")
    void already_apply_error() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        LectureRegs lectureRegs = new LectureRegs(employeeNumber, lectureId);
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        Lecture lecture = new Lecture("김준우", "1강연장", 5, startAt, "스프링 강연");

        when(lectureRepository.findById(lectureId)).thenReturn(lecture);
        when(lectureRegsRepository.existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(), lectureRegs.lectureId())).thenReturn(true);

        // when && then
        CoreException thrown = assertThrows(CoreException.class, () -> lectureService.apply("", lectureRegs));
        assertThat(thrown.getErrorType().getCode()).isEqualTo(CoreErrorCode.E1002);
        assertThat(thrown.getErrorType().getMessage()).isEqualTo("Already applied for a lecture.");
        verify(lectureRepository, times(1)).findById(lectureId);
        verify(lectureRegsRepository, times(1)).existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(), lectureRegs.lectureId());
    }

    @Test
    @DisplayName("강연 참석 인원이 정원에 도달하면 실패한다")
    void exceeded_lecture_apply_error() {
        // given
        final int employeeNumber = 12345;
        final Long lectureId = 1L;
        LectureRegs lectureRegs = new LectureRegs(employeeNumber, lectureId);
        LocalDateTime startAt = LocalDateTime.of(2024, 3, 27, 0, 0, 0);
        Lecture lecture = new Lecture("김준우", "1강연장", 5, startAt, "스프링 강연");

        when(lectureRepository.findById(lectureId)).thenReturn(lecture);
        when(lectureRegsRepository.existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(), lectureRegs.lectureId())).thenReturn(false);
        when(lectureRegsRepository.countByLectureId(lectureId)).thenReturn(5);

        // when && then
        CoreException thrown = assertThrows(CoreException.class, () -> lectureService.apply("", lectureRegs));
        assertThat(thrown.getErrorType().getCode()).isEqualTo(CoreErrorCode.E1001);
        assertThat(thrown.getErrorType().getMessage()).isEqualTo("Lecture has been exceeded.");
        verify(lectureRepository, times(1)).findById(lectureId);
        verify(lectureRegsRepository, times(1)).existsByEmployeeNumberAndLectureId(lectureRegs.employeeNumber(), lectureRegs.lectureId());
        verify(lectureRegsRepository, times(1)).countByLectureId(lectureId);
    }
}