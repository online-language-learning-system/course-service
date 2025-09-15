package com.hub.course_service.dto.category;

import com.hub.course_service.model.enumeration.Level;

public record CourseCategoryPostDto(
        String name,
        Level level,
        String description
) {
}
