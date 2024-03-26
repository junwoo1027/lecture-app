package io.lecture.core.api.controller.v1;

import io.lecture.core.api.controller.v1.request.NewLectureRequest;
import io.lecture.core.api.controller.v1.response.NewLectureResponse;
import io.lecture.core.api.domain.Lecture;
import io.lecture.core.api.domain.LectureService;
import io.lecture.core.api.support.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lectures")
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping
    public ApiResponse<NewLectureResponse> newLecture(@RequestBody NewLectureRequest request) {
        return ApiResponse.success(new NewLectureResponse(lectureService.append(request.toLecture())));
    }
}
