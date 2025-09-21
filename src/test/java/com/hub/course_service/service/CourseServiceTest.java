package com.hub.course_service.service;

import com.hub.course_service.repository.CourseRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


// @SpringBootTest(classes = CourseServiceApplication.class)        // -- Use this annotation for Integration Testing
@ExtendWith(MockitoExtension.class)                                 // -- Enable using @Mock and @InjectMocks
public class CourseServiceTest {

    private static final String TITLE = "Basic Japanese for Beginners";
    private static final String DESCRIPTION  = "This course is designed for anyone starting their Japanese learning journey from scratch. " +
            "You will learn the Hiragana and Katakana alphabets, proper pronunciation, " +
            "essential daily vocabulary, and simple conversation patterns. " +
            "By the end of the course, you will be able to introduce yourself, greet others, " +
            "and communicate confidently in basic everyday situations.";

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

//    @Test
//    void givenValidDto_whenCreateCourse_thenCourseIsSavedSuccessfully() {
//
//        // ASSUMPTIONS
//        CoursePostDto coursePostDto = new CoursePostDto(TITLE, DESCRIPTION);
//        Course course = new Course();
//        course.setId(1L);
//        course.setTitle(TITLE);
//        course.setDescription(DESCRIPTION);
//        given(courseRepository.save(any(Course.class))).willReturn(course);
//
//        // CALL
//        Course savedCourse = courseService.create(coursePostDto);
//
//        // VALIDATIONS
//        assertThat(savedCourse).isNotNull();
//        assertThat(savedCourse.getId()).isEqualTo(1L);
//        assertThat(savedCourse.getTitle()).isEqualTo(TITLE);
//        assertThat(savedCourse.getDescription()).isEqualTo(DESCRIPTION);
//
//        // verify(courseRepository).save(any(Course.class));
//        verify(courseRepository).save(
//                argThat(saved ->
//                        saved.getTitle().equals(TITLE)
//                        && saved.getDescription().equals(DESCRIPTION)
//                )
//        );
//
//    }

//    @Test
//    void get_course_successfully() {
//
//        // ASSUMPTIONS
//        Long id = 1L;
//
//        Course course = new Course();
//        course.setId(id);
//        course.setTitle(TITLE);
//        course.setDescription(DESCRIPTION);
//        given(courseRepository.findById(id)).willReturn(Optional.of(course));
//
//        // WHEN
//        CourseDetailGetDto courseDetailGetDto = courseService.getCourseById(id);
//
//        // VALIDATIONS
//        assertThat(courseDetailGetDto).isNotNull();
//        assertThat(courseDetailGetDto.title()).isEqualTo(TITLE);
//        assertThat(courseDetailGetDto.shortDescription()).isEqualTo(DESCRIPTION);
//
//        verify(courseRepository).findById(any(Long.class));
//
//    }

}
