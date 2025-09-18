package com.hub.course_service.model.dto.lesson;

import java.util.List;

public record LessonPostDto(
        String title,
        String description,
        Integer duration,
        List<LessonResourcePostDto> resources
) {
}
