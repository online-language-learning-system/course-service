package com.hub.course_service.model.dto.module;

import com.hub.course_service.model.CourseModule;

public record CourseModuleDetailGetDto(
        Long id,
        Long courseId,
        String title,
        String description,
        int orderIndex,
        boolean canFreeTrial
) {
    public static CourseModuleDetailGetDto courseModuleDetailGetDto(CourseModule courseModule) {
        return new CourseModuleDetailGetDto(
                courseModule.getId(), courseModule.getCourse().getId(),
                courseModule.getTitle(), courseModule.getDescription(),
                courseModule.getOrderIndex(), courseModule.isCanFreeTrial()
        );
    }
}
