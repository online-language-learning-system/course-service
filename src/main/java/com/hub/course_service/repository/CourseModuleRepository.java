package com.hub.course_service.repository;

import com.hub.course_service.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseModuleRepository
        extends JpaRepository<CourseModule, Long> {

    @Query("SELECT module FROM CourseModule module " +
            "WHERE module.course = :courseId")
    Iterable<CourseModule> findModuleByCourseId(@Param(value = "courseId") Long courseId);

}
