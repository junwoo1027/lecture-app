package io.lecture.domain.lecture.domain;

import io.lecture.domain.error.CoreErrorCode;
import io.lecture.domain.error.CoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LectureTest {

    Lecture lecture;

    @BeforeEach
    void setup() {
        lecture = new Lecture(1L, "준우", "1강연장", 10, LocalDateTime.now(), "스프링 강연");
    }

    @Test
    @DisplayName("강연 좌석 보다 카운트가 많으면 에러를 던진다")
    void isExceeded() {
        CoreException thrown = assertThrows(CoreException.class, () -> lecture.isExceeded(11));
        assertThat(thrown.getErrorType().getCode()).isEqualTo(CoreErrorCode.E1001);
        assertThat(thrown.getErrorType().getMessage()).isEqualTo("Lecture has been exceeded.");
    }

}