package io.lecture.core.api.controller.v1;

import io.lecture.core.api.controller.v1.request.ApplyLectureRequest;
import io.lecture.core.api.controller.v1.request.CancelLectureRequest;
import io.lecture.core.api.controller.v1.request.NewLectureRequest;
import io.lecture.core.api.controller.v1.response.*;
import io.lecture.core.api.support.response.ApiResponse;
import io.lecture.domain.lecture.Lecture;
import io.lecture.domain.lecture.LectureRegs;
import io.lecture.domain.lecture.LectureService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    /** 강연 등록 */
    @PostMapping("/lectures")
    public ApiResponse<NewLectureResponse> newLecture(@Valid @RequestBody NewLectureRequest request) {
        Long successId = lectureService.append(request.toNewLecture());
        return ApiResponse.success(NewLectureResponse.of(successId));
    }

    /** 전체 강연 목록 조회 */
    @GetMapping("/lectures")
    public ApiResponse<List<FindLectureResponse>> findLectures() {
        List<Lecture> lectures = lectureService.findLectures();
        return ApiResponse.success(FindLectureResponse.of(lectures));
    }

    /** 강연 신청 */
    @PostMapping("/lectures/{lectureId}/apply")
    public ApiResponse<ApplyLectureResponse> applyLecture(
            @PathVariable Long lectureId,
            @Valid @RequestBody ApplyLectureRequest request
    ) {
        Long successId = lectureService.apply("apply_lecture", request.toLectureRegs(lectureId));
        return ApiResponse.success(ApplyLectureResponse.of(successId));
    }

    /** 강연 취소 */
    @PostMapping("/lectures/{lectureId}/cancel")
    public ApiResponse cancelLecture(
            @PathVariable Long lectureId,
            @Valid @RequestBody CancelLectureRequest request
            ) {
        lectureService.cancel(request.toCancelLectureRegs(lectureId));
        return ApiResponse.success();
    }

    /** 강연 별 신청자 목록 조회 */
    @GetMapping("/lectures/{lectureId}/employee")
    public ApiResponse<List<FindEmployeeListResponse>> findEmployeeList(
            @PathVariable Long lectureId
    ) {
        List<LectureRegs> lectureRegsList = lectureService.findLectureRegistrationsByLecture(lectureId);
        return ApiResponse.success(FindEmployeeListResponse.of(lectureRegsList));
    }

    /** 신청한 강연 목록 조회 */
    @GetMapping("/employee/{employeeNumber}/lectures")
    public ApiResponse<List<FindLectureForEmployeeResponse>> FindLecturesForEmployeeResponse(
            @PathVariable("employeeNumber") Integer employeeNumberNumber
    ) {
        List<Lecture> lectures = lectureService.findLecturesByEmployee(employeeNumberNumber);
        return ApiResponse.success(FindLectureForEmployeeResponse.of(lectures));
    }
}
