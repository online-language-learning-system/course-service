package com.hub.course_service.model.dto.course;

import com.hub.course_service.model.Course;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CourseDetailGetDto(
        Long id,
        String title,
        String teachingLanguage,
        BigDecimal price,
        String level,
        String description,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        String createdBy,
        OffsetDateTime createOn,
        String lastModifiedBy,
        OffsetDateTime lastModifiedOn
) {
    public static CourseDetailGetDto fromModel(Course course) {
        return new CourseDetailGetDto(
            course.getId(), course.getTitle(), course.getTeachingLanguage(),
            course.getPrice(), course.getCourseCategory().getCategoryLevel().toString(),
            course.getDescription(),
            course.getStartDate(), course.getEndDate(),
            course.getCreatedBy(), course.getCreatedOn(),
            course.getLastModifiedBy(), course.getLastModifiedOn()
        );
    }
}
