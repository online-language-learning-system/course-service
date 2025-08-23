package com.hub.course_service.service;

import com.hub.common_library.exception.DuplicatedException;
import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.dto.course.CoursePostDto;
import com.hub.course_service.dto.course.CourseGetDetailDto;
import com.hub.course_service.model.Course;
import com.hub.course_service.repository.CourseRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseGetDetailDto getCourseById(Long id) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, id));

        return CourseGetDetailDto.fromModel(course);
    }

    public Course create(CoursePostDto coursePostDto) {
        validateDuplicateName(coursePostDto.title(), null);

        Course course = new Course();
        course.setTitle(coursePostDto.title());
        if (!coursePostDto.description().isEmpty())
            course.setDescription(coursePostDto.description());

        return courseRepository.save(course);
    }

    private boolean checkExistedName(String name, Long id) {
        return courseRepository.findExistedName(name, id) != null;
    }

    private void validateDuplicateName(String name, Long id) {
        if (checkExistedName(name, id)) {
            throw new DuplicatedException(Constants.ErrorCode.NAME_ALREADY_EXITED, name);
        }
    }

}
