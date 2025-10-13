package com.capstone.assessment_service.repository;

import com.capstone.assessment_service.model.AssessmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<AssessmentEntity, UUID> {
    Optional<AssessmentEntity> findByAssessmentName(String name);
    Page<AssessmentEntity> findByAssessmentNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("""
    SELECT a
    FROM AssessmentEntity a
    WHERE a.capsule.id = :capsuleId
    """)
    List<AssessmentEntity> findByCapsuleId(@Param("capsuleId") UUID capsuleId);
}
