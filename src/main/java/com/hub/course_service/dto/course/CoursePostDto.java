package com.hub.course_service.dto.course;

import com.hub.course_service.validation.ValidateProductPrice;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record CoursePostDto(

        @NotBlank String title,
        @NotBlank String teachingLanguage,
        @ValidateProductPrice BigDecimal price,
        String description,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        String createdBy,
        String lastModifiedBy

) {
}
