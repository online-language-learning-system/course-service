package com.hub.course_service.repository;

import com.hub.course_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository
        extends JpaRepository<Course, Long> {

    @Query("SELECT e FROM courses WHERE e.name = ?1 AND (?2 IS NULL OR e.id != ?2)")
    Course findExistedName(String name, Long id);

}
