package com.hub.course_service.repository;

import com.hub.course_service.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseModuleRepository
        extends JpaRepository<CourseModule, Long> {

}
