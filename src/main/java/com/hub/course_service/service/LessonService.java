package com.hub.course_service.service;

import com.hub.course_service.client.MediaServiceClient;
import com.hub.course_service.model.CourseModule;
import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.LessonResource;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.model.enumeration.ResourceType;
import com.hub.course_service.repository.LessonRepository;
import com.hub.course_service.repository.LessonResourceRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;

@Service
public class LessonService {

    private final MediaServiceClient mediaServiceClient;
    private final LessonRepository lessonRepository;
    private final LessonResourceRepository lessonResourceRepository;

    public LessonService(MediaServiceClient mediaServiceClient,
                         LessonRepository lessonRepository,
                         LessonResourceRepository lessonResourceRepository) {
        this.mediaServiceClient = mediaServiceClient;
        this.lessonRepository = lessonRepository;
        this.lessonResourceRepository = lessonResourceRepository;
    }

    public Lesson createLesson(CourseModule courseModule,
                               LessonPostDto lessonPostDto,
                               LinkedList<MultipartFile> resourceFiles) {
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

                        if (!resourceFiles.isEmpty() && !lessonResource.getResourceType().equals(ResourceType.TEXT)) {
                            String resourceUrl = uploadResourceFiles(resourceFiles.getFirst());
                            lessonResource.setResourceUrl(resourceUrl);
                            resourceFiles.removeFirst();
                        } else {
                            lessonResource.setResourceUrl(null);
                        }

                        savedMainLesson.getLessonResources().add(lessonResource);
                        lessonResourceRepository.save(lessonResource);
                    }
                );
        }

        return lessonRepository.save(savedMainLesson);
    }

    private String uploadResourceFiles(MultipartFile file) {
        ResponseEntity<String> response = mediaServiceClient.uploadFile(file);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException(Constants.ErrorCode.UPLOAD_FILE_FAILED + " " + response.getStatusCode());
        }
    }

}
