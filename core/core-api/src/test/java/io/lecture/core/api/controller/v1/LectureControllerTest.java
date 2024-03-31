package io.lecture.core.api.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lecture.core.api.controller.v1.request.ApplyLectureRequest;
import io.lecture.core.api.controller.v1.request.CancelLectureRequest;
import io.lecture.core.api.controller.v1.request.NewLectureRequest;
import io.lecture.domain.lecture.LectureService;
import io.lecture.domain.lecture.domain.Lecture;
import io.lecture.domain.lecture.domain.LectureRegs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class LectureControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private LectureService lectureService;

    @Test
    @DisplayName("[POST] 강연 등록 테스트")
    void lecture() throws Exception {
        // given
        NewLectureRequest request = new NewLectureRequest(
                "준우",
                "1강연장",
                10,
                LocalDateTime.now(),
                "스프링"
        );
        Long successId = 1L;

        when(lectureService.append(request.toNewLecture())).thenReturn(successId);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(successId));
    }

    @Test
    @DisplayName("[GET] 전체 강연 목록 조회")
    void findLectures() throws Exception {
        // given
        List<Lecture> lectures = createLecturesData();

        given(lectureService.findLectures()).willReturn(lectures);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/lectures")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].lecturer").value(lectures.get(0).lecturer()))
                .andExpect(jsonPath("$.data[0].hall").value(lectures.get(0).hall()))
                .andExpect(jsonPath("$.data[0].seats").value(lectures.get(0).seats()))
                .andExpect(jsonPath("$.data[0].startAt").exists())
                .andExpect(jsonPath("$.data[0].description").value(lectures.get(0).description()))
                .andExpect(jsonPath("$.data[1].lecturer").value(lectures.get(1).lecturer()))
                .andExpect(jsonPath("$.data[1].hall").value(lectures.get(1).hall()))
                .andExpect(jsonPath("$.data[1].seats").value(lectures.get(1).seats()))
                .andExpect(jsonPath("$.data[1].startAt").exists())
                .andExpect(jsonPath("$.data[1].description").value(lectures.get(1).description()));
    }

    @Test
    @DisplayName("[POST] 강연 신청 테스트")
    void applyLecture() throws Exception {
        // given
        Integer employeeNumber = 12345;
        ApplyLectureRequest request = new ApplyLectureRequest(employeeNumber);
        Long lectureId = 1L;

        when(lectureService.apply("apply_lecture", request.toLectureRegs(lectureId))).thenReturn(lectureId);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lectures/1/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(lectureId));
    }

    @Test
    @DisplayName("[POST] 강연 취소 테스트")
    void cancelLecture() throws Exception {
        // given
        Integer employeeNumber = 12345;
        CancelLectureRequest request = new CancelLectureRequest(employeeNumber);
        Long lectureId = 1L;

        //when && then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lectures/" + lectureId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));
        verify(lectureService, times(1)).cancel(request.toCancelLectureRegs(lectureId));
    }

    @Test
    @DisplayName("[GET] 강연 별 신청자 목록 조회 테스트")
    void findEmployeeList() throws Exception {
        // given
        Long lectureId = 1L;
        List<LectureRegs> lecturesRegsList = createLecutreRegsListData(lectureId);

        given(lectureService.findLectureRegistrationsByLecture(lectureId)).willReturn(lecturesRegsList);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/lectures/" + lectureId + "/employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].employeeNumber").value(lecturesRegsList.get(0).employeeNumber()))
                .andExpect(jsonPath("$.data[1].employeeNumber").value(lecturesRegsList.get(1).employeeNumber()));
    }

    @Test
    @DisplayName("[GET] 신청한 강연 목록 조회 테스트")
    void FindLecturesForEmployee() throws Exception {
        // given
        Integer employeeNumber = 12345;
        List<Lecture> lectures = createLecturesData();

        given(lectureService.findLecturesByEmployee(employeeNumber)).willReturn(lectures);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/" + employeeNumber + "/lectures")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].lecturer").value(lectures.get(0).lecturer()))
                .andExpect(jsonPath("$.data[0].hall").value(lectures.get(0).hall()))
                .andExpect(jsonPath("$.data[0].seats").value(lectures.get(0).seats()))
                .andExpect(jsonPath("$.data[0].startAt").exists())
                .andExpect(jsonPath("$.data[0].description").value(lectures.get(0).description()))
                .andExpect(jsonPath("$.data[1].lecturer").value(lectures.get(1).lecturer()))
                .andExpect(jsonPath("$.data[1].hall").value(lectures.get(1).hall()))
                .andExpect(jsonPath("$.data[1].seats").value(lectures.get(1).seats()))
                .andExpect(jsonPath("$.data[1].startAt").exists())
                .andExpect(jsonPath("$.data[1].description").value(lectures.get(1).description()));
    }

    @Test
    @DisplayName("[GET] 최근 3일간 인기강연 목록 조회 테스트")
    void findPopularLectures() throws Exception {
        // given
        List<Lecture> lectures = createLecturesData();

        given(lectureService.findPopularLectures()).willReturn(lectures);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/lectures/recent-popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].lecturer").value(lectures.get(0).lecturer()))
                .andExpect(jsonPath("$.data[0].hall").value(lectures.get(0).hall()))
                .andExpect(jsonPath("$.data[0].seats").value(lectures.get(0).seats()))
                .andExpect(jsonPath("$.data[0].startAt").exists())
                .andExpect(jsonPath("$.data[0].description").value(lectures.get(0).description()))
                .andExpect(jsonPath("$.data[1].lecturer").value(lectures.get(1).lecturer()))
                .andExpect(jsonPath("$.data[1].hall").value(lectures.get(1).hall()))
                .andExpect(jsonPath("$.data[1].seats").value(lectures.get(1).seats()))
                .andExpect(jsonPath("$.data[1].startAt").exists())
                .andExpect(jsonPath("$.data[1].description").value(lectures.get(1).description()));
    }

    private static List<Lecture> createLecturesData() {
        List<Lecture> lectures = List.of(
                new Lecture(
                        1L,
                        "준우",
                        "1강연장",
                        10,
                        LocalDateTime.of(2024, 3, 01, 0, 0),
                        "스프링"
                ),
                new Lecture(
                        2L,
                        "준우2",
                        "2강연장",
                        20,
                        LocalDateTime.of(2024, 3, 31, 0, 0),
                        "자바"
                )
        );
        return lectures;
    }

    private static List<LectureRegs> createLecutreRegsListData(Long lectureId) {
        List<LectureRegs> lecturesRegsList = List.of(
                new LectureRegs(
                        1L,
                        11111,
                        lectureId
                ),
                new LectureRegs(
                        2L,
                        22222,
                        lectureId
                )
        );
        return lecturesRegsList;
    }
}
