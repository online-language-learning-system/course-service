package com.hub.course_service.service;

import com.hub.course_service.client.MediaServiceClient;
import com.hub.course_service.model.Course;
import com.hub.course_service.model.CourseImage;
import com.hub.course_service.repository.CourseImageRepository;
import com.hub.course_service.utils.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseImageService {

    private final MediaServiceClient mediaServiceClient;
    private final CourseImageRepository courseImageRepository;

    public CourseImageService(MediaServiceClient mediaServiceClient,
                              CourseImageRepository courseImageRepository) {
        this.mediaServiceClient = mediaServiceClient;
        this.courseImageRepository = courseImageRepository;
    }

    // Upload image file to S3 Bucket
    public String uploadCourseImage(MultipartFile file) {
        ResponseEntity<String> response = mediaServiceClient.uploadFile(file);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException(Constants.ErrorCode.UPLOAD_FILE_FAILED + " " + response.getStatusCode());
        }
    }

    // Save image url from S3 Bucket to Database
    public List<CourseImage> saveImageUrls(List<String> imageUrls, Course course) {

        List<CourseImage> courseImages = imageUrls.stream().map(
            imageUrl -> {
                CourseImage courseImage = new CourseImage();
                courseImage.setImageUrl(imageUrl);
                courseImage.setCourse(course);
                return courseImage;
            }
        ).collect(Collectors.toList());

        // collect(Collectors.toList()) -- Mutable List
        // toList() -- Immutable List

        courseImageRepository.saveAll(courseImages);
        return courseImages;
    }

    public CourseImage saveImageUrl(String imageUrl, Course course) {

        CourseImage courseImage = new CourseImage();
        courseImage.setImageUrl(imageUrl);
        courseImage.setCourse(course);

        // collect(Collectors.toList()) -- Mutable List
        // toList() -- Immutable List

        courseImageRepository.save(courseImage);
        return courseImage;
    }

}
