package com.hub.course_service.controller;

import com.hub.course_service.dto.course.CourseGetDetailDto;
import com.hub.course_service.dto.course.CoursePostDto;
import com.hub.course_service.dto.error.ErrorDto;
import com.hub.course_service.model.Course;
import com.hub.course_service.service.CourseService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/backoffice/courses/{id}")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Ok",
                content = @Content(
                    schema = @Schema(implementation = CourseGetDetailDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)
                )
            )
        }
    )
    public ResponseEntity<CourseGetDetailDto> getCourseDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping("/backoffice/courses")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Created",
                content = @Content(
                        schema = @Schema(implementation = CourseGetDetailDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(
                    schema = @Schema(implementation = ErrorDto.class)
                )
            )
        }
    )
    public ResponseEntity<CourseGetDetailDto> createCourse(
            @RequestBody CoursePostDto coursePostDto,
            UriComponentsBuilder uriComponentsBuilder,
            Principal principal
    ) {
        Course course = courseService.create(coursePostDto);
        CourseGetDetailDto courseGetDetailDto = CourseGetDetailDto.fromModel(course);

        URI uri = uriComponentsBuilder
                .replacePath("/courses/{id}")
                .buildAndExpand(course.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(courseGetDetailDto);
    }

}
