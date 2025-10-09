package com.hub.course_service.grpc;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.grpc.lesson.LessonIdsResponse;
import com.hub.course_service.grpc.lesson.LessonServiceGrpc;
import com.hub.course_service.grpc.module.CourseIdRequest;
import com.hub.course_service.grpc.module.ModuleListResponse;
import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.dto.course.CourseDetailGetDto;
import com.hub.course_service.repository.CourseRepository;
import com.hub.course_service.service.CourseService;
import com.hub.course_service.service.LessonService;
import com.hub.course_service.utils.Constants;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.hub.course_service.model.Course;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonServiceImpl extends LessonServiceGrpc.LessonServiceImplBase {

    public final CourseRepository courseRepository;

    @Override
    public void getLessonIdsForCourse(CourseIdRequest request,
                                      StreamObserver<LessonIdsResponse> responseObserver) {

        try {

            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, request.getCourseId()));

//            List<Long> lessonIds = new ArrayList<>();
//            course.getCourseModules().forEach(
//                    module -> module.getLessons().forEach(
//                            lesson -> lessonIds.add(lesson.getId())
//                    )
//            );

            List<Long> lessonIds = course.getCourseModules().stream()
                    .flatMap(module -> module.getLessons().stream())
                    .map(lesson -> lesson.getId())
                    .toList();

            LessonIdsResponse lessonIdsResponse = LessonIdsResponse.newBuilder()
                    .addAllLessonIds(lessonIds)
                    .build();

            responseObserver.onNext(lessonIdsResponse);
            responseObserver.onCompleted();

        } catch (NotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(Constants.ErrorCode.COURSE_NOT_FOUND + ": " + e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Unexpected error: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
