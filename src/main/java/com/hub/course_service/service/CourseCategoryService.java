package com.hub.course_service.service;

import com.hub.common_library.exception.DuplicatedException;
import com.hub.course_service.dto.category.CourseCategoryPostDto;
import com.hub.course_service.model.CourseCategory;
import com.hub.course_service.repository.CourseCategoryRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.stereotype.Service;

@Service
public class CourseCategoryService {

    private final CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryService(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    public CourseCategory create(CourseCategoryPostDto courseCategoryPostDto) {

        validateDuplicateName(courseCategoryPostDto.name(), null);

        CourseCategory courseCategory = new CourseCategory();
        courseCategory.setName(courseCategoryPostDto.name());
        courseCategory.setCategoryLevel(courseCategoryPostDto.categoryLevel());
        courseCategory.setDescription(courseCategory.getDescription());
        return courseCategoryRepository.save(courseCategory);
    }

    private boolean checkExistedName(String name, Long id) {
        return courseCategoryRepository.findExistedName(name, id) != null;
    }

    private void validateDuplicateName(String name, Long id) {
        if (checkExistedName(name, id)) {
            throw new DuplicatedException(Constants.ErrorCode.NAME_ALREADY_EXITED, name);
        }
    }
}
