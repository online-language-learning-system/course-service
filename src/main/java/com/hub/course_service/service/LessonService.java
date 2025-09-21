package com.hub.course_service.service;

import com.hub.course_service.model.CourseModule;
import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.LessonResource;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.repository.LessonRepository;
import com.hub.course_service.repository.LessonResourceRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonResourceRepository lessonResourceRepository;

    public LessonService(LessonRepository lessonRepository,
                         LessonResourceRepository lessonResourceRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonResourceRepository = lessonResourceRepository;
    }

    public Lesson createLesson(CourseModule courseModule, LessonPostDto lessonPostDto) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonPostDto.title());
        lesson.setDescription(lessonPostDto.description());
        lesson.setDuration(lessonPostDto.duration());
        lesson.setCourseModule(courseModule);
        Lesson savedMainLesson = lessonRepository.save(lesson);

        // Create lesson resources
        if (lessonPostDto.resources() != null) {
            lessonPostDto.resources()
                .forEach(
                        resource -> {
                    LessonResource lessonResource = new LessonResource();
                    lessonResource.setLesson(savedMainLesson);
                    lessonResource.setResourceType(resource.resourceType());
                    lessonResource.setResourceUrl(resource.resourceUrl());
                    savedMainLesson.getLessonResources().add(lessonResource);
                    lessonResourceRepository.save(lessonResource);
                }
            );
        }

        return lessonRepository.save(savedMainLesson);
    }

}
