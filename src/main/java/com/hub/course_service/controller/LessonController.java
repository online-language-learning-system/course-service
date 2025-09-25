package com.hub.course_service.controller;

import com.hub.course_service.model.dto.lesson.LessonDetailGetDto;
import com.hub.course_service.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/storefront/{moduleId}/lessons")
    public ResponseEntity<List<LessonDetailGetDto>> getAllLessonByModuleId(@PathVariable(value = "moduleId") Long moduleId) {
        return ResponseEntity.ok(lessonService.getAllLessonByModuleId(moduleId));
    }
}
