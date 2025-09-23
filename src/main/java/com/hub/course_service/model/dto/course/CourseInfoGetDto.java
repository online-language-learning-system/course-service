package com.hub.course_service.model.dto.course;

import com.hub.course_service.model.Course;

import java.math.BigDecimal;

public record CourseInfoGetDto(
        Long id,
        String title,
        BigDecimal price,
        String teachingLanguage,
        String level,
        String imagePresignedUrl
) {
    // Static factory method
    // Constructor
    // Builder
    public static CourseInfoGetDto fromModel(Course course, String imagePresignedUrl) {
        return new CourseInfoGetDto(
            course.getId(),
            course.getTitle(),
            course.getPrice(),
            course.getTeachingLanguage(),
            course.getCourseCategory().getCategoryLevel().toString(),
            imagePresignedUrl
        );
    }
}

