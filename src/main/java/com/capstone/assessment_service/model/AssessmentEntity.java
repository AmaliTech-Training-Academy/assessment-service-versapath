package com.capstone.assessment_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assessment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "assessment_name", nullable = false)
    private String assessmentName;

    @Column(name = "assessment_type")
    private String assessmentType;

    @Column(name = "max_attempts")
    private int maxAttempts;

    @Column(name = "time_limit_minutes")
    private int timeLimitMinutes;

    @Column(name = "passing_score")
    private int passingScore;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "moodle_quiz_id")
    private int moodleQuizId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_capsule_id", nullable = false)
    private SkillCapsuleEntity capsule;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
