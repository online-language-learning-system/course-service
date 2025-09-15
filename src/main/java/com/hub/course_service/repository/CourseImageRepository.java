package com.hub.course_service.repository;

import com.hub.course_service.model.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseImageRepository
        extends JpaRepository<CourseImage, Long> {
}
