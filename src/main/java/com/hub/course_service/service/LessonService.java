package com.hub.course_service.service;

import com.hub.common_library.exception.NotFoundException;
import com.hub.course_service.model.CourseModule;
import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.LessonResource;
import com.hub.course_service.model.dto.lesson.LessonDetailGetDto;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.model.dto.lesson.LessonResourcePostDto;
import com.hub.course_service.repository.CourseModuleRepository;
import com.hub.course_service.repository.LessonRepository;
import com.hub.course_service.repository.LessonResourceRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.stereotype.Service;

@Service
public class LessonService {

    private final CourseModuleRepository courseModuleRepository;
    private final LessonRepository lessonRepository;
    private final LessonResourceRepository lessonResourceRepository;

    public LessonService(CourseModuleRepository courseModuleRepository,
                         LessonRepository lessonRepository,
                         LessonResourceRepository lessonResourceRepository) {
        this.courseModuleRepository = courseModuleRepository;
        this.lessonRepository = lessonRepository;
        this.lessonResourceRepository = lessonResourceRepository;
    }

    public LessonDetailGetDto createLesson(Long moduleId, LessonPostDto lessonPostDto) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonPostDto.title());
        lesson.setDescription(lessonPostDto.description());
        lesson.setDuration(lessonPostDto.duration());

        CourseModule courseModule = setCourseModule(moduleId, lesson);
        courseModuleRepository.save(courseModule);
        Lesson savedMainLesson = lessonRepository.save(lesson);


        // Create lesson resources
        if (lessonPostDto.resources() != null) {
            lessonPostDto.resources()
                .forEach(resource -> {
                        LessonResource src = createResources(resource, savedMainLesson);
                        lessonResourceRepository.save(src);
                    }
                );
        }

        lessonRepository.save(savedMainLesson);
        return LessonDetailGetDto.fromModel(lesson);
    }

    private CourseModule setCourseModule(Long courseModuleId, Lesson lesson) {
        CourseModule courseModule = courseModuleRepository
                .findById(courseModuleId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_MODULE_NOT_FOUND, courseModuleId));

        lesson.setCourseModule(courseModule);
        courseModule.getLessons().add(lesson);

        return courseModule;
    }

    private LessonResource createResources(LessonResourcePostDto lessonResourcePostDto, Lesson savedMainLesson) {

        // Get lesson by ID
        Lesson lesson = lessonRepository
            .findById(savedMainLesson.getId())
            .orElseThrow(() ->
                new NotFoundException(Constants.ErrorCode.LESSON_NOT_FOUND, savedMainLesson.getId())
            );

        LessonResource lessonResource = new LessonResource();
        lessonResource.setLesson(lesson);
        lessonResource.setResourceType(lessonResourcePostDto.resourceType());
        lessonResource.setResourceUrl(lessonResourcePostDto.resourceUrl());

        lesson.getLessonResources().add(lessonResource);
        lessonRepository.save(lesson);

        return lessonResource;
    }
}
