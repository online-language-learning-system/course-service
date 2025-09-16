package com.hub.course_service.service;

import com.hub.common_library.exception.DuplicatedException;
import com.hub.common_library.exception.InvalidDateRangeException;
import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.dto.course.CoursePostDto;
import com.hub.course_service.dto.course.CourseDetailGetDto;
import com.hub.course_service.model.Course;
import com.hub.course_service.model.CourseCategory;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import com.hub.course_service.repository.CourseCategoryRepository;
import com.hub.course_service.repository.CourseRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;

    public CourseService(CourseRepository courseRepository,
                         CourseCategoryRepository courseCategoryRepository) {
        this.courseRepository = courseRepository;
        this.courseCategoryRepository = courseCategoryRepository;
    }

//    public CourseDetailGetDto getCourseById(Long id) {
//
//        Course course = courseRepository
//                .findById(id)
//                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, id));
//
//        Long categoryId = course.getCategoryId().getId();
//        CourseCategory courseCategory = courseCategoryRepository.findById(course.getCategoryId().getId())
//                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CATEGORY_NOT_FOUND, categoryId))
//        String level = courseCategory.getCategoryLevel().toString();
//
//        return CourseDetailGetDto.fromModel(course, level);
//    }

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

//    public CourseListGetDto getCoursesWithFilter(int pageNo, int pageSize, String courseTitle) {
//
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        Page<Course> coursePage = courseRepository.findCoursesWithFilter(courseTitle.trim().toLowerCase(), pageable);
//
//        List<Course> courses = coursePage.getContent();
//        List<CourseDetailGetDto> courseDetailGetDtos = courses.stream()
//                .map(course -> CourseDetailGetDto.fromModel(course))
//                .toList();
//
//        return new CourseListGetDto(
//                courseDetailGetDtos,
//                coursePage.getNumber(),
//                coursePage.getSize(),
//                (int) coursePage.getTotalElements(),
//                coursePage.getTotalPages(),
//                coursePage.isLast()
//        );
//    }

//    public CourseListGetDto getTrialCourse(int pageNo, int pageSize, BigDecimal price) {
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        Page<Course> coursePage = courseRepository.findTrialCourse(price, pageable);
//
//        List<Course> courses = coursePage.getContent();
//        List<CourseDetailGetDto> courseDetailGetDtos = courses.stream()
//                .map(course -> CourseDetailGetDto.fromModel(course))
//                .toList();
//
//        return new CourseListGetDto(
//                courseDetailGetDtos,
//                coursePage.getNumber(),
//                coursePage.getSize(),
//                (int) coursePage.getTotalElements(),
//                coursePage.getTotalPages(),
//                coursePage.isLast()
//        );
//    }

    public CourseDetailGetDto create(CoursePostDto coursePostDto) {

        Course course = new Course();

        validateDuplicateTitle(coursePostDto.title(), null);
        course.setTitle(coursePostDto.title());

        validateEndDateMustGreaterThanStartDate(coursePostDto);
        course.setStartDate(coursePostDto.startDate());
        course.setEndDate(coursePostDto.endDate());

        course.setTeachingLanguage(coursePostDto.teachingLanguage());
        course.setPrice(coursePostDto.price());

        if (!coursePostDto.description().isEmpty())
            course.setDescription(coursePostDto.description());

        CourseCategory courseCategory = setCourseCategory(coursePostDto.categoryId(), course);
        CourseCategory savedCourseCategory = courseCategoryRepository.save(courseCategory);
        String level = savedCourseCategory.getCategoryLevel().toString();

        ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
        course.setApprovalStatus(approvalStatus);
        Course savedCourse = courseRepository.save(course);

        return CourseDetailGetDto.fromModel(savedCourse, level);

    }

    private CourseCategory setCourseCategory(Long categoryId, Course course) {
        CourseCategory courseCategory = courseCategoryRepository
                        .findById(categoryId)
                        .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CATEGORY_NOT_FOUND, categoryId));
        course.setCourseCategory(courseCategory);
        return courseCategory;
    }

    // Title Validation
    private boolean checkExistedTitle(String title, Long id) {
        return courseRepository.findExistedName(title, id) != null;
    }

    private void validateDuplicateTitle(String title, Long id) {
        if (checkExistedTitle(title, id)) {
            throw new DuplicatedException(Constants.ErrorCode.TITLE_ALREADY_EXITED, title);
        }
    }

    // Date Range Validation
    private boolean checkDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        return endDate.isAfter(startDate);
    }

    private void validateEndDateMustGreaterThanStartDate(CoursePostDto coursePostDto) {
        if (checkDateRange(coursePostDto.startDate(), coursePostDto.endDate())) {
            throw new InvalidDateRangeException(Constants.ErrorCode.END_DATE_MUST_AFTER_START_DATE);
        }
    }

}
