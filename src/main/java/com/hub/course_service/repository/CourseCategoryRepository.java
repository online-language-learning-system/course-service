package com.hub.course_service.repository;

import com.hub.course_service.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCategoryRepository
        extends JpaRepository<CourseCategory, Long> {

    @Query("SELECT e FROM CourseCategory e WHERE e.name = :name AND (:name IS NULL OR e.id != :id)")
    Boolean findExistedName(String name, Long id);

}
