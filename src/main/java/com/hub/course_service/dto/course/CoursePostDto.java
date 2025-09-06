package com.hub.course_service.dto.course;

import java.math.BigDecimal;

public record CoursePostDto(
        String title, BigDecimal price, String description
) {
}
