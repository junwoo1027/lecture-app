package io.lecture.core.api.controller.v1;

import io.lecture.core.api.controller.v1.request.ApplyLectureRequest;
import io.lecture.core.api.controller.v1.request.NewLectureRequest;
import io.lecture.core.api.controller.v1.response.ApplyLectureResponse;
import io.lecture.core.api.controller.v1.response.FindLectureResponse;
import io.lecture.core.api.controller.v1.response.NewLectureResponse;
import io.lecture.core.api.support.response.ApiResponse;
import io.lecture.domain.lecture.Lecture;
import io.lecture.domain.lecture.LectureService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lectures")
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    /** 강연 등록 */
    @PostMapping()
    public ApiResponse<NewLectureResponse> newLecture(@Valid @RequestBody NewLectureRequest request) {
        Long successId = lectureService.append(request.toLecture());
        return ApiResponse.success(NewLectureResponse.of(successId));
    }

    /** 전체 강연 목록 조회 */
    @GetMapping
    public ApiResponse<List<FindLectureResponse>> findAllLectures() {
        List<Lecture> lectures = lectureService.findAll();
        return ApiResponse.success(FindLectureResponse.of(lectures));
    }

    /** 강연 신청 */
    @PostMapping("/{lectureId}/apply")
    public ApiResponse<ApplyLectureResponse> applyLecture(
            @PathVariable Long lectureId,
            @Valid @RequestBody ApplyLectureRequest request
    ) {
        Long successId = lectureService.apply("apply_lecture", request.toLectureRegs(lectureId));
        return ApiResponse.success(ApplyLectureResponse.of(successId));
    }
}
