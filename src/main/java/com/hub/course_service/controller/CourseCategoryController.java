package com.hub.course_service.controller;

import com.hub.course_service.service.CourseCategoryService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseCategoryController {

    private final CourseCategoryService courseCategoryService;

    public CourseCategoryController(CourseCategoryService courseCategoryService) {
        this.courseCategoryService = courseCategoryService;
    }

}
