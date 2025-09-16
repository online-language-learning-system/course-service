package com.hub.course_service.model;

import com.hub.common_library.model.AbstractAuditEntity;
import com.hub.course_service.model.enumeration.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "course", schema = "dbo")
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
    private CourseCategory courseCategory;  // FK in DB

    private String title;

    @Column(name = "teaching_language")
    private String teachingLanguage;

    @Column(nullable = false, precision = 9, scale = 0)
    private BigDecimal price;

    private String description;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;      // Course approval by admin

    @OneToMany(mappedBy = "course")
    private List<CourseImage> courseImages;

    @OneToMany(mappedBy = "course")
    private List<CourseModule> courseModules;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        if (getCreatedOn() == null) setCreatedOn(now);
        if (getLastModifiedOn() == null) setLastModifiedOn(now);
    }

}
