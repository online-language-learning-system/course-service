package com.hub.course_service.model.dto.image;

import com.hub.course_service.model.CourseImage;

public record CourseImageDetailGetDto(
        Long imageId,
        Long courseId,
        String imageUrl
) {
    public static CourseImageDetailGetDto fromModel(CourseImage courseImage) {
        return new CourseImageDetailGetDto(
                courseImage.getId(),
                courseImage.getCourse().getId(),
                courseImage.getImageUrl()
        );
    }
}
