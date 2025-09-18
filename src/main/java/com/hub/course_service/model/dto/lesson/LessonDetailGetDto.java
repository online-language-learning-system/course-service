package com.hub.course_service.model.dto.lesson;

import com.hub.course_service.model.Lesson;

public record LessonDetailGetDto(
        Long id,
        Long moduleId,
        String title,
        String description,
        Integer duration
) {
    public static LessonDetailGetDto fromModel(Lesson lesson) {
        return new LessonDetailGetDto(
                lesson.getId(), lesson.getCourseModule().getId(),
                lesson.getTitle(), lesson.getDescription(), lesson.getDuration()
        );
    }
}
