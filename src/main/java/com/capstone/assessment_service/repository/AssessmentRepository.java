package com.capstone.assessment_service.repository;

import com.capstone.assessment_service.model.AssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<AssessmentEntity, UUID> {
    Optional<AssessmentEntity> findByAssessmentName(String name);
    Optional<AssessmentEntity> findByMoodleQuizId(int moodleQuizId);
}
