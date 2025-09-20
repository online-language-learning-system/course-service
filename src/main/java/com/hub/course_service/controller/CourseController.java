package com.hub.course_service.controller;

import com.hub.course_service.model.dto.course.CourseDetailGetDto;
import com.hub.course_service.model.dto.course.CourseDetailListGetDto;
import com.hub.course_service.model.dto.course.CoursePostDto;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import com.hub.course_service.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Get Pending Course
    @GetMapping(path = "/backoffice/courses/pending")   // ADMIN
    public ResponseEntity<CourseDetailListGetDto> getPendingCourses(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return ResponseEntity.ok(courseService.getPendingCourseList(pageNo, pageSize));
    }

    @GetMapping("/storefront/courses")
    public ResponseEntity<CourseDetailListGetDto> getCoursesByTitle(
            @RequestParam(name = "courseTitle", defaultValue = "", required = false) String courseTitle,
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "9", required = false) int pageSize
    ) {
        return ResponseEntity.ok(courseService.getCoursesWithFilter(pageNo, pageSize, courseTitle));
    }

    @PostMapping(value = "/backoffice/courses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseDetailGetDto> createCourse(@RequestPart("coursePostDto") CoursePostDto coursePostDto,
                                                           @RequestPart("multipartFiles") MultipartFile multipartFiles) {
        CourseDetailGetDto courseDetailGetDto = courseService.createCourse(coursePostDto, multipartFiles);
        return new ResponseEntity<>(courseDetailGetDto, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/backoffice/{courseId}/courses")
    public ResponseEntity<Void> updateCourseApprovalStatus(@PathVariable(name = "courseId") Long courseId,
                                                           @RequestParam(name = "approvalStatus") ApprovalStatus approvalStatus) {
        courseService.updateCourseApprovalStatus(courseId, approvalStatus);
        return ResponseEntity.noContent().build();
    }

}



/*
        @PathVariable Long id
        endpoint ("/users/{id}")
        GET /users/123

        @RequestParam String name , @RequestParam int page
        endpoint ("/users")
        GET /users?name=John&page=2
 */