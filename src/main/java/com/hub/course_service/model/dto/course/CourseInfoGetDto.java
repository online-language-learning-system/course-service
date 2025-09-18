package com.hub.course_service.model.dto.course;

import com.hub.course_service.model.Course;

import java.math.BigDecimal;

public record CourseInfoGetDto(
        Long id,
        String title,
        String teachingLanguage,
        BigDecimal price,
        String level,
        String description
) {
    // Static factory method
    // Constructor
    // Builder
    public static CourseInfoGetDto fromModel(Course course) {
        return new CourseInfoGetDto(
                course.getId(),
                course.getTitle(),
                course.getTeachingLanguage(),
                course.getPrice(),
                course.getCourseCategory().getCategoryLevel().toString(),
                course.getDescription()
        );
    }
}

