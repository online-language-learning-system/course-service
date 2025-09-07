package com.hub.course_service.dto.course;

import com.hub.course_service.model.Course;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record CourseDetailGetDto(
        Long id,
        String title,
        String teaching_language,
        BigDecimal price,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static CourseDetailGetDto fromModel(Course course) {
        return new CourseDetailGetDto(
                course.getId(), course.getTitle(), course.getTeachingLanguage(),
                course.getPrice(), course.getDescription(),
                course.getStartDate(), course.getEndDate(),
                course.getCreatedOn(), course.getLastModifiedOn());
    }
}
