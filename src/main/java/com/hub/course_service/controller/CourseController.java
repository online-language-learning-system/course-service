package com.hub.course_service.controller;

import com.hub.course_service.model.dto.course.CourseDetailGetDto;
import com.hub.course_service.model.dto.course.CoursePostDto;
import com.hub.course_service.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

//    @GetMapping("/backoffice/courses/{id}")
//    public ResponseEntity<CourseDetailGetDto> getCourseDetailById(@PathVariable Long id) {
//        return ResponseEntity.ok(courseService.getCourseById(id));
//    }
//
//    @GetMapping("/storefront/courses")
//    public ResponseEntity<CourseListGetDto> getCoursesByTitle(
//            @RequestParam(name = "courseTitle", defaultValue = "", required = false) String courseTitle,
//            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
//            @RequestParam(name = "pageSize", defaultValue = "9", required = false) int pageSize
//    ) {
//        return ResponseEntity.ok(courseService.getCoursesWithFilter(pageNo, pageSize, courseTitle));
//    }

//    @GetMapping("/storefront/courses/trial")
//    public ResponseEntity<CourseListGetDto> getCoursesByTitle(
//            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
//            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
//    ) {
//        return ResponseEntity.ok(courseService.getTrialCourse(pageNo, pageSize, BigDecimal.ZERO));
//    }

    @PostMapping(path = "/backoffice/courses", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CourseDetailGetDto> createCourse(@RequestBody CoursePostDto coursePostDto) {
        CourseDetailGetDto courseDetailGetDto = courseService.createCourse(coursePostDto);

//        URI uri = uriComponentsBuilder
//                .replacePath("/courses/{id}")
//                .buildAndExpand(course.getId()).toUri();

        return new ResponseEntity<>(courseDetailGetDto, HttpStatus.CREATED);
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