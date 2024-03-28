package io.lecture.core.api.controller.v1;

import io.lecture.core.api.controller.v1.request.NewLectureRequest;
import io.lecture.core.api.controller.v1.response.FindLectureResponse;
import io.lecture.core.api.controller.v1.response.NewLectureResponse;
import io.lecture.core.api.support.response.ApiResponse;
import io.lecture.domain.Lecture;
import io.lecture.domain.LectureService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lectures")
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    // 강연 등록
    @PostMapping()
    public ApiResponse<NewLectureResponse> newLecture(@RequestBody NewLectureRequest request) {
        Long successId = lectureService.append(request.toLecture());
        return ApiResponse.success(NewLectureResponse.of(successId));
    }

    // 시작 시간 1주일 전부터 시작 시간 1일 후
    @GetMapping("/active")
    public ApiResponse<List<FindLectureResponse>> findLectures() {
        List<Lecture> lectures = lectureService.find();
        return ApiResponse.success(FindLectureResponse.of(lectures));
    }

    // 전체 강연 목록 조회
    @GetMapping
    public ApiResponse<List<FindLectureResponse>> findAllLectures() {
        List<Lecture> lectures = lectureService.findAll();
        return ApiResponse.success(FindLectureResponse.of(lectures));
    }
}
