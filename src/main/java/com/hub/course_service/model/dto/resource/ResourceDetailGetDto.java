package com.hub.course_service.model.dto.resource;

import com.hub.course_service.model.LessonResource;
import com.hub.course_service.model.enumeration.ResourceType;

public record ResourceDetailGetDto(
        Long id,
        Long lessonId,
        ResourceType resourceType,
        String resourceUrl
) {
    public static ResourceDetailGetDto fromModel(LessonResource lessonResource) {
        return new ResourceDetailGetDto(
                lessonResource.getId(),
                lessonResource.getLesson().getId(),
                lessonResource.getResourceType(),
                lessonResource.getResourceUrl()
        );
    }
}
