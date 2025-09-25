package com.hub.course_service.service;

import com.hub.course_service.client.MediaServiceClient;
import com.hub.course_service.model.CourseModule;
import com.hub.course_service.model.Lesson;
import com.hub.course_service.model.LessonResource;
import com.hub.course_service.model.dto.lesson.LessonDetailGetDto;
import com.hub.course_service.model.dto.lesson.LessonPostDto;
import com.hub.course_service.model.dto.resource.ResourceDetailGetDto;
import com.hub.course_service.model.enumeration.ResourceType;
import com.hub.course_service.repository.LessonRepository;
import com.hub.course_service.repository.LessonResourceRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final MediaServiceClient mediaServiceClient;
    private final LessonRepository lessonRepository;
    private final LessonResourceService lessonResourceService;
    private final LessonResourceRepository lessonResourceRepository;

    public LessonService(MediaServiceClient mediaServiceClient,
                         LessonRepository lessonRepository,
                         LessonResourceService lessonResourceService,
                         LessonResourceRepository lessonResourceRepository) {
        this.mediaServiceClient = mediaServiceClient;
        this.lessonRepository = lessonRepository;
        this.lessonResourceRepository = lessonResourceRepository;
        this.lessonResourceService = lessonResourceService;
    }

//    public List<LessonDetailGetDto> getAllLessonByModuleId(Long moduleId) {
//        List<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId);
//
//        Map<Long, String> resourceMap = new HashMap<>();
//        List<LessonDetailGetDto> lessonDetailGetDtos = new ArrayList<>();
//        for (Lesson mainLesson : lessons) {
//
//            List<ResourceDetailGetDto> resourceDetailGetDtos = new ArrayList<>();
//            for (LessonResource mainResource : mainLesson.getLessonResources()) {
//                String resourceUrlFromBucket = lessonResourceService.getResourceUrlById(mainResource.getId());
//                // resourceDetailGetDtos.add(ResourceDetailGetDto.fromModel(mainResource, resourceUrlFromBucket));
//                resourceMap.put(mainResource.getId(), resourceUrlFromBucket);
//            }
//
////            ResourceDetailGetDto resourceDetailGetDto = new ResourceDetailGetDto(resourceMap)
//            for (Long key : resourceMap.keySet()) {
//                mainLesson.getLessonResources().stream()
//                        .map(lessonResource -> {
//                            return lessonResource.getId() == key ? resourceMap.get(key) : null;
//                        }
//                ).collect(Collectors.toList());
//            }
//
//        }
//    }

    public List<LessonDetailGetDto> getAllLessonByModuleId(Long moduleId) {
        List<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId);

        List<LessonDetailGetDto> lessonDetailGetDtos = new ArrayList<>();

        for (Lesson lesson : lessons) {

            List<ResourceDetailGetDto> resourceDetailGetDtos = lesson.getLessonResources().stream()
                    .map(resource -> {
                        String resourceUrl = lessonResourceService.getResourceUrlById(resource.getId());
                        return ResourceDetailGetDto.fromModel(resource, resourceUrl);
                    })
                    .collect(Collectors.toList());

            LessonDetailGetDto lessonDto = new LessonDetailGetDto(
                    lesson.getId(),
                    lesson.getCourseModule().getId(),
                    lesson.getTitle(),
                    lesson.getDescription(),
                    lesson.getDuration(),
                    resourceDetailGetDtos
            );

            lessonDetailGetDtos.add(lessonDto);
        }

        return lessonDetailGetDtos;
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
