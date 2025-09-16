package com.hub.course_service.model;

import com.hub.common_library.model.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "app", name = "course_module")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseModule extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    private String title;

    private String description;

    @Column(name = "order_index")
    private int orderIndex;

    @Column(name = "can_free_trial")
    private Boolean canFreeTrial;

}
