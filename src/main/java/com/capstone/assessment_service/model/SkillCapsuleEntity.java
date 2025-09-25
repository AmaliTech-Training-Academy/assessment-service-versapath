package com.capstone.assessment_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "skill_capsules_snapshot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillCapsuleEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String difficulty;
    private String proficiencyLevel;
    private int moodleCourseId;
    private int estimatedHours;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
