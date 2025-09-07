package com.hub.course_service.repository;

import com.hub.course_service.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository
        extends JpaRepository<Course, Long> {

    @Query("SELECT entity FROM Course entity WHERE entity.title = :title AND (:title IS NULL OR entity.id != :id)")
    Course findExistedName(String title, Long id);

    @Query("SELECT c FROM Course c "
            + "WHERE LOWER(c.title) LIKE %:courseTitle% "
            + "ORDER BY c.id ASC")
    Page<Course> findCoursesWithFilter(@Param("courseTitle") String courseTitle, Pageable pageable);
}
