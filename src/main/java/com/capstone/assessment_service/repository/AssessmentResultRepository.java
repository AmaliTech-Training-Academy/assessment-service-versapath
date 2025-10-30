package com.capstone.assessment_service.repository;

import com.capstone.assessment_service.dto.assessment.AssessmentResultResponseDto;
import com.capstone.assessment_service.model.AssessmentResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResultEntity, UUID> {

    /* fetch all assessments under a capsule and if there is an assessment result of a user fetch them as well.*/
    @Query("""
    SELECT new com.capstone.assessment_service.dto.assessment.AssessmentResultResponseDto(
            a.assessmentName, a.capsule.name, a.description, a.maxAttempts, COALESCE(ar.attemptNumber, 0),
            a.maxAttempts - COALESCE(ar.attemptNumber, 0), a.passingScore, COALESCE(ar.score, 0.0),
            COALESCE(ar.isPassed, false), a.moodleQuizId
        )
        FROM AssessmentEntity a
        LEFT JOIN AssessmentResultEntity ar ON a.id = ar.assessment.id AND ar.learner.id = :learnerId
        WHERE a.capsule.id = :capsuleId
""")
    List<AssessmentResultResponseDto> findAssessmentWithLearnerResult(
            @Param("learnerId") UUID learnerId,
            @Param("capsuleId") UUID capsuleId);
}
