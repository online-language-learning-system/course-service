package com.hub.course_service.dto.course;

import com.hub.course_service.validation.ValidateCoursePrice;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public record CoursePostDto(

        Long categoryId,
        @NotBlank String title,
        @NotBlank String teachingLanguage,
        @ValidateCoursePrice BigDecimal price,
        String description,
        OffsetDateTime startDate,
        OffsetDateTime endDate

) {
}
