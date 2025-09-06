package com.hub.course_service.dto.course;

import com.hub.course_service.model.Course;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record CourseGetDetailDto(
        Long id,
        String title,
        BigDecimal price,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static CourseGetDetailDto fromModel(Course course) {
        return new CourseGetDetailDto(
                course.getId(), course.getTitle(), course.getPrice(), course.getDescription(),
                course.getStartDate(), course.getEndDate(),
                course.getCreatedOn(), course.getLastModifiedOn());
    }
}
