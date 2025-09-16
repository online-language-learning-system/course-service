package com.hub.course_service.model;

import com.hub.common_library.model.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "courses", schema = "app")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // Many Courses to one Category
    @JoinColumn(name = "category_id")
    private CourseCategory categoryId;

    private String title;

    @Column(name = "teaching_language")
    private String teachingLanguage;

    private String description;

    @Column(nullable = false, precision = 9, scale = 0)
    private BigDecimal price;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "approval_status")
    private Boolean approvalStatus;      // Course approval by admin

    @PrePersist
    public void prePersist() {
        ZonedDateTime now = ZonedDateTime.now();
        if (getCreatedOn() == null) setCreatedOn(now);
        if (getLastModifiedOn() == null) setLastModifiedOn(now);
    }

}
