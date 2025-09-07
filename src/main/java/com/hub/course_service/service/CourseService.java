package com.hub.course_service.service;

import com.hub.common_library.exception.DuplicatedException;
import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.dto.course.CourseListGetDto;
import com.hub.course_service.dto.course.CoursePostDto;
import com.hub.course_service.dto.course.CourseDetailGetDto;
import com.hub.course_service.model.Course;
import com.hub.course_service.repository.CourseRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseDetailGetDto getCourseById(Long id) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, id));

        return CourseDetailGetDto.fromModel(course);
    }

    /*
    public List<CourseGetDetailDto> getCoursesByName(String courseTitle, int pageNumber, int pageSize) {
        if (courseTitle.isEmpty())
            return null;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Course> coursePage = courseRepository.findAllByTitle(courseTitle, pageable);

        if (coursePage.hasContent()) {
            return coursePage.stream()
                    .map(CourseGetDetailDto::fromModel)
                    .toList();
        } else {
            throw new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, courseTitle);
        }
    }
     */

    public CourseListGetDto getCoursesWithFilter(int pageNo, int pageSize, String courseTitle) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Course> coursePage = courseRepository.findCoursesWithFilter(courseTitle.trim().toLowerCase(), pageable);

        List<Course> courses = coursePage.getContent();
        List<CourseDetailGetDto> courseDetailGetDtos = courses.stream()
                .map(course -> CourseDetailGetDto.fromModel(course))
                .toList();

        return new CourseListGetDto(
                courseDetailGetDtos,
                coursePage.getNumber(),
                coursePage.getSize(),
                (int) coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                coursePage.isLast()
        );
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
