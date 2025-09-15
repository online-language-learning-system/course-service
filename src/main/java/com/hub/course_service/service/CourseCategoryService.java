package com.hub.course_service.service;

import com.hub.course_service.dto.category.CourseCategoryPostDto;
import com.hub.course_service.model.CourseCategory;
import com.hub.course_service.repository.CourseCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseCategoryService {

    private final CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryService(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    public CourseCategory create(CourseCategoryPostDto courseCategoryPostDto) {
        CourseCategory courseCategory = new CourseCategory();
        courseCategory.setName(courseCategoryPostDto.name());
        courseCategory.setLevel(courseCategoryPostDto.level());
        courseCategory.setDescription(courseCategory.getDescription());
        return courseCategoryRepository.save(courseCategory);
    }
}
