package com.hub.course_service.service;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.model.Course;
import com.hub.course_service.model.dto.module.CourseModuleDetailGetDto;
import com.hub.course_service.model.dto.module.CourseModulePostDto;
import com.hub.course_service.model.CourseModule;
import com.hub.course_service.repository.CourseModuleRepository;
import com.hub.course_service.repository.CourseRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.stereotype.Service;

@Service
public class CourseModuleService {

    private final CourseRepository courseRepository;
    private final CourseModuleRepository courseModuleRepository;

    public CourseModuleService(CourseRepository courseRepository,
                               CourseModuleRepository courseModuleRepository) {
        this.courseRepository = courseRepository;
        this.courseModuleRepository = courseModuleRepository;
    }

    public CourseModuleDetailGetDto createModule(Long courseId, CourseModulePostDto courseModulePostDto) {
        CourseModule courseModule = new CourseModule();
        courseModule.setTitle(courseModulePostDto.title());
        courseModule.setDescription(courseModulePostDto.description());
        courseModule.setOrderIndex(courseModulePostDto.orderIndex());
        courseModule.setCanFreeTrial(courseModulePostDto.canFreeTrial());

        // add module to course
        Course course = setCourse(courseId, courseModule);
        courseModuleRepository.save(courseModule);

        return CourseModuleDetailGetDto.courseModuleDetailGetDto(courseModule);
    }

    // Set relationship in owning side (many-to-one)
    private Course setCourse(Long courseId, CourseModule courseModule) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, courseId));

        courseModule.setCourse(course);
        course.getCourseModules().add(courseModule);
        return course;
    }

}
