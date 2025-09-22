package com.hub.course_service.repository;

import com.hub.course_service.model.Course;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CourseRepository
        extends JpaRepository<Course, Long> {

    @Query("SELECT entity FROM Course entity " +
            "WHERE entity.title = :title " +
            "AND (:title IS NULL OR entity.id != :id)")
    Course findExistedName(String title, Long id);

    @Query("SELECT c FROM Course c "
            + "WHERE LOWER(c.title) LIKE %:courseTitle% "
            + "AND (c.approvalStatus = APPROVED)"
            + "ORDER BY c.id ASC")
    Page<Course> findCoursesWithFilter(@Param("courseTitle") String courseTitle, Pageable pageable);

    @Query("SELECT course FROM Course course " +
            "WHERE course.price = 0")
    Page<Course> findFreeCourse(@Param("price") BigDecimal price, Pageable pageable);

    @Query("SELECT course FROM Course course " +
            "WHERE course.approvalStatus = :approvalStatus")
    Page<Course> findCoursesByApprovalStatus(@Param("status") ApprovalStatus approvalStatus, Pageable pageable);

}
