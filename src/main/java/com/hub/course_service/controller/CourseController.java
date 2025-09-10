package com.hub.course_service.controller;

import com.hub.course_service.dto.course.CourseDetailGetDto;
import com.hub.course_service.dto.course.CourseListGetDto;
import com.hub.course_service.dto.course.CoursePostDto;
import com.hub.course_service.dto.error.ErrorDto;
import com.hub.course_service.model.Course;
import com.hub.course_service.service.CourseService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/backoffice/courses/{id}")
    public ResponseEntity<CourseDetailGetDto> getCourseDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/storefront/courses")
    public ResponseEntity<CourseListGetDto> getCoursesByTitle(
            @RequestParam(name = "courseTitle", defaultValue = "", required = false) String courseTitle,
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return ResponseEntity.ok(courseService.getCoursesWithFilter(pageNo, pageSize, courseTitle));
    }

    @GetMapping("/storefront/courses/trial")
    public ResponseEntity<CourseListGetDto> getCoursesByTitle(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return ResponseEntity.ok(courseService.getTrialCourse(pageNo, pageSize, BigDecimal.ZERO));
    }

    @PostMapping("/backoffice/courses")
    public ResponseEntity<CourseDetailGetDto> createCourse(
            @RequestBody CoursePostDto coursePostDto,
            UriComponentsBuilder uriComponentsBuilder,
            Principal principal
    ) {
        Course course = courseService.create(coursePostDto);
        CourseDetailGetDto courseDetailGetDto = CourseDetailGetDto.fromModel(course);

        URI uri = uriComponentsBuilder
                .replacePath("/courses/{id}")
                .buildAndExpand(course.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(courseDetailGetDto);
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