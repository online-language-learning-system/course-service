package com.hub.course_service.service;

import com.hub.common_library.exception.DuplicatedException;
import com.hub.common_library.exception.InvalidDateRangeException;
import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.model.*;
import com.hub.course_service.model.dto.course.CourseInfoGetDto;
import com.hub.course_service.model.dto.course.CourseInfoListGetDto;
import com.hub.course_service.model.dto.course.CoursePostDto;
import com.hub.course_service.model.dto.course.CourseDetailGetDto;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.model.dto.module.CourseModulePostDto;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import com.hub.course_service.repository.*;
import com.hub.course_service.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseImageService courseImageService;
    private final CourseModuleService courseModuleService;
    private final LessonService lessonService;

    public CourseService(CourseRepository courseRepository,
                         CourseCategoryRepository courseCategoryRepository,
                         CourseImageService courseImageService,
                         CourseModuleService courseModuleService,
                         LessonService lessonService) {
        this.courseRepository = courseRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.courseImageService = courseImageService;
        this.courseModuleService = courseModuleService;
        this.lessonService = lessonService;
    }

    public CourseInfoListGetDto getCoursesWithFilter(String courseTitle, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Course> coursePage = courseRepository.findCoursesWithFilter(courseTitle.trim().toLowerCase(), pageable);
        return toCourseInfoListGetDto(coursePage);
    }

    public CourseInfoListGetDto getFreeCourse(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Course> coursePage = courseRepository.findFreeCourse(BigDecimal.ZERO, pageable);
        return toCourseInfoListGetDto(coursePage);
    }

    public CourseInfoListGetDto getPendingCourseList(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Course> coursePage = courseRepository.findCoursesByApprovalStatus(ApprovalStatus.PENDING, pageable);
        return toCourseInfoListGetDto(coursePage);
    }

    public CourseDetailGetDto getDetailCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, id));
        return CourseDetailGetDto.fromModel(course);
    }

    /*
     * ORCHESTRATOR
           try {
                 start transaction
                 call intercepted method
                 commit transaction
            } catch (RuntimeException e) {
                 rollback transaction
            }
     * @Transactional - ACID principal
            Automatically rollback when meeting RuntimeException or Error
            -- Avoid multiple saving in Repository
     */
    @Transactional
    public CourseDetailGetDto createCourse(CoursePostDto coursePostDto,
                                           MultipartFile courseImage,
                                           LinkedList<MultipartFile> resourceFiles) {
        Course course = new Course();

        // Set common information of course
        validateDuplicateTitle(coursePostDto.title(), null);
        course.setTitle(coursePostDto.title());
        course.setTeachingLanguage(coursePostDto.teachingLanguage());
        course.setPrice(coursePostDto.price());

        validateEndDateMustGreaterThanStartDate(coursePostDto);
        course.setStartDate(coursePostDto.startDate());
        course.setEndDate(coursePostDto.endDate());

        if (!coursePostDto.description().isEmpty())
            course.setDescription(coursePostDto.description());

        ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
        course.setApprovalStatus(approvalStatus);

        // Course categories
        setCourseCategory(coursePostDto.categoryId(), course);

        // Prioritize saving the course first to get its id
        Course savedMainCourse = courseRepository.save(course);
        log.info("COURSE SERVICE: Completely set common information of course");

        // Set Image for Course
        String imageUrl = courseImageService.uploadCourseImage(courseImage);

        CourseImage savedMainCourseImage = courseImageService.saveImageUrl(imageUrl, savedMainCourse);
        List<CourseImage> courseImages = new ArrayList<>();
        courseImages.add(savedMainCourseImage);
        savedMainCourse.setCourseImages(courseImages);
        log.info("COURSE SERVICE: Completely set image to course");

        // Set Module
        for (CourseModulePostDto module : coursePostDto.courseModules()) {
            CourseModule savedMainCourseModule = courseModuleService.createModule(savedMainCourse, module);

            // Set Lesson
            for (LessonPostDto lessonPostDto : module.lessons()) {
                Lesson lesson = lessonService.createLesson(savedMainCourseModule, lessonPostDto, resourceFiles);
                courseModuleService.addLessonToModule(lesson, savedMainCourseModule);
            }
        }
        log.info("COURSE SERVICE: Completely set module and lesson to course");

        savedMainCourse = courseRepository.save(savedMainCourse);

        return CourseDetailGetDto.fromModel(savedMainCourse);
    }

    public void updateCourseApprovalStatus(Long courseId, ApprovalStatus approvalStatus) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, courseId));
        course.setApprovalStatus(approvalStatus);
        courseRepository.save(course);
    }

    private void setCourseCategory(Long categoryId, Course course) {

        CourseCategory courseCategory = courseCategoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CATEGORY_NOT_FOUND, categoryId));

        course.setCourseCategory(courseCategory);

        courseCategory.getCourses().add(course);
        courseCategoryRepository.save(courseCategory);
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
        if (!checkDateRange(coursePostDto.startDate(), coursePostDto.endDate())) {
            throw new InvalidDateRangeException(Constants.ErrorCode.END_DATE_MUST_AFTER_START_DATE);
        }
    }

    private CourseInfoListGetDto toCourseInfoListGetDto(Page<Course> coursePage) {
        List<CourseInfoGetDto> courseInfoGetDtos = coursePage.getContent()
                .stream()
                .map(CourseInfoGetDto::fromModel)
                .toList();

        return new CourseInfoListGetDto(
                courseInfoGetDtos,
                coursePage.getNumber(),
                coursePage.getSize(),
                (int) coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                coursePage.isLast()
        );
    }

}
