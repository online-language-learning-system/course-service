package com.hub.course_service.repository;

import com.hub.course_service.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository
        extends JpaRepository<Course, Long> {

    @Query("SELECT entity FROM Course entity WHERE entity.title = :title AND (:title IS NULL OR entity.id != :id)")
    Course findExistedName(String title, Long id);

    Page<Course> findAllByTitle(String title, Pageable pageable);

}
