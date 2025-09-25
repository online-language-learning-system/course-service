package com.hub.course_service.repository;

import com.hub.course_service.model.Course;
import com.hub.course_service.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseModuleRepository
        extends JpaRepository<CourseModule, Long> {

    @Query("SELECT module FROM CourseModule module " +
            "WHERE module.course = :courseId")
    Iterable<CourseModule> findModuleByCourseId(@Param(value = "courseId") Long courseId);

    @Query("SELECT module FROM CourseModule module " +
            "WHERE :courseId IS NOT NULL " +
            "AND module.course.id = :courseId")
    List<CourseModule> findAllByCourseId(Long courseId);

}
