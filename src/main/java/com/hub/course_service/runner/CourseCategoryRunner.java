package com.hub.course_service.runner;

import com.hub.course_service.model.CourseCategory;
import com.hub.course_service.model.enumeration.CategoryLevel;
import com.hub.course_service.repository.CourseCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseCategoryRunner implements CommandLineRunner {

    private final CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryRunner(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        for (CategoryLevel level : CategoryLevel.values()) {
            CourseCategory category = new CourseCategory();
            category.setCategoryLevel(level);

            switch (level) {
                case N5 -> category.setDescription(
                    "Basic Beginner: \n" +
                    "Can understand and use basic Japanese phrases and expressions. " +
                    "Able to read and comprehend hiragana, katakana, and very simple kanji. " +
                    "Can introduce oneself, ask simple questions, and have short everyday conversations."
                );
                case N4 -> category.setDescription(
                    "Upper Beginner: \n" +
                    "Can understand basic daily conversations spoken slowly. " +
                    "Can read and understand basic sentences using common vocabulary and kanji. " +
                    "Can manage simple communication in everyday life."
                );
                case N3 -> category.setDescription(
                    "Intermediate: \n" +
                    "Can understand everyday situations with more complex conversations. " +
                    "Can read texts with specific content such as headlines, essays, or letters."
                );
                case N2 -> category.setDescription(
                    "Upper Intermediate: \n" +
                    "Can understand general topics and conversations in depth at natural speed. " +
                    "Can read editorials, essays, newspapers, and magazines."
                );
                case N1 -> category.setDescription(
                    "Advanced: \n" +
                    "Can understand Japanese used in a wide range of contexts, " +
                    "including abstract or highly complex topics. Can comprehend sophisticated texts " +
                    "such as academic papers and literary works."
                );
            }

            courseCategoryRepository.save(category);
        }

    }
}
