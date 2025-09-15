package com.hub.course_service.model;

import com.hub.common_library.model.AbstractAuditEntity;
import com.hub.course_service.model.enumeration.Level;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course_category", schema = "app")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategory extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;

    private String description;

}
