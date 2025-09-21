package com.hub.course_service.model.dto.course;

import com.hub.course_service.model.Course;
import com.hub.course_service.model.dto.image.CourseImageDetailGetDto;
import com.hub.course_service.model.dto.module.CourseModuleDetailGetDto;
import com.hub.course_service.model.enumeration.ApprovalStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CourseDetailGetDto(
        Long id,
        String title,
        String teachingLanguage,
        BigDecimal price,
        String level,
        String description,
        ApprovalStatus approvalStatus,

        OffsetDateTime startDate,
        OffsetDateTime endDate,

        List<CourseModuleDetailGetDto> modules,
        List<CourseImageDetailGetDto> images,

        String createdBy,
        OffsetDateTime createOn,
        String lastModifiedBy,
        OffsetDateTime lastModifiedOn
) {
    public static CourseDetailGetDto fromModel(Course course) {

        return new CourseDetailGetDto(
            course.getId(),
            course.getTitle(),
            course.getTeachingLanguage(),
            course.getPrice(),
            course.getCourseCategory().getCategoryLevel().toString(),
            course.getDescription(),
            course.getApprovalStatus(),
            course.getStartDate(),
            course.getEndDate(),
            course.getCourseModules().stream()

                .map(courseModule -> CourseModuleDetailGetDto.fromModel(courseModule))
                .collect(Collectors.toList()),

            course.getCourseImages().stream()
                    .map(image -> {return CourseImageDetailGetDto.fromModel(image);})
                    .collect(Collectors.toList()),

            course.getCreatedBy(),
            course.getCreatedOn(),
            course.getLastModifiedBy(),
            course.getLastModifiedOn()
        );

    }
}
