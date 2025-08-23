package com.hub.course_service.repository;

import com.hub.course_service.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository
        extends JpaRepository<Lesson, Long> {

}
