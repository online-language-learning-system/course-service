package com.hub.course_service.repository;

import com.hub.course_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository
        extends JpaRepository<Course, Long> {

}
