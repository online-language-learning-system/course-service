package com.hub.course_service.model.dto.course;


import java.util.List;

public record CourseDetailListGetDto(
        List<CourseDetailGetDto> courseContent,
        int pageNo,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
