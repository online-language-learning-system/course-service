package com.hub.course_service.service;

import com.hub.common_library.exception.DuplicatedException;
import com.hub.common_library.exception.InvalidDateRangeException;
import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.model.*;
import com.hub.course_service.model.dto.course.CoursePostDto;
import com.hub.course_service.model.dto.course.CourseDetailGetDto;
import com.hub.course_service.model.dto.lesson.LessonDetailGetDto;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.model.dto.module.CourseModuleDetailGetDto;
import com.hub.course_service.model.dto.module.CourseModulePostDto;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import com.hub.course_service.repository.*;
import com.hub.course_service.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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

//
//    public List<CourseGetDetailDto> getCoursesByName(String courseTitle, int pageNumber, int pageSize) {
//        if (courseTitle.isEmpty())
//            return null;
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        Page<Course> coursePage = courseRepository.findAllByTitle(courseTitle, pageable);
//
//        if (coursePage.hasContent()) {
//            return coursePage.stream()
//                    .map(CourseGetDetailDto::fromModel)
//                    .toList();
//        } else {
//            throw new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND, courseTitle);
//        }
//    }


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
    public CourseDetailGetDto createCourse(CoursePostDto coursePostDto, MultipartFile courseImage) {

        Course course = new Course();

        // Title Validation
        validateDuplicateTitle(coursePostDto.title(), null);
        course.setTitle(coursePostDto.title());

        // Date Range Validation
        validateEndDateMustGreaterThanStartDate(coursePostDto);
        course.setStartDate(coursePostDto.startDate());
        course.setEndDate(coursePostDto.endDate());

        // Remaining attributes of the Course
        course.setTeachingLanguage(coursePostDto.teachingLanguage());
        course.setPrice(coursePostDto.price());

        if (!coursePostDto.description().isEmpty())
            course.setDescription(coursePostDto.description());

        ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
        course.setApprovalStatus(approvalStatus);

        // Set Category
        setCourseCategory(coursePostDto.categoryId(), course);
        Course savedMainCourse = courseRepository.save(course);     // Prioritize saving the course first to get its id
        log.info("Completely set common attribute course");

        // Set Image for Course
//        List<String> imageUrls = new ArrayList<>();
//        for (MultipartFile file : coursePostDto.multipartFiles()) {
//            String imageUrl = courseImageService.uploadCourseImage(file);
//            imageUrls.add(imageUrl);
//        }
        String imageUrl = courseImageService.uploadCourseImage(courseImage);

        CourseImage savedMainCourseImage = courseImageService.saveImageUrl(imageUrl, savedMainCourse);
        List<CourseImage> courseImages = new ArrayList<>();
        courseImages.add(savedMainCourseImage);
        savedMainCourse.setCourseImages(courseImages);
        log.info("Completely set image to course");

        // Set Module
        for (CourseModulePostDto module : coursePostDto.courseModules()) {
            Long courseId = savedMainCourse.getId();
            CourseModuleDetailGetDto courseModuleDetailGetDto = courseModuleService.createModule(courseId, module);

            // Set Lesson
            for (LessonPostDto lessonPostDto : module.lessons()) {
                Long moduleId = courseModuleDetailGetDto.id();
                LessonDetailGetDto lessonDetailGetDto = lessonService.createLesson(moduleId, lessonPostDto);
            }
        }
        log.info("Completely set module and lesson to course");

        savedMainCourse = courseRepository.save(savedMainCourse);

        return CourseDetailGetDto.fromModel(savedMainCourse);
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

}
