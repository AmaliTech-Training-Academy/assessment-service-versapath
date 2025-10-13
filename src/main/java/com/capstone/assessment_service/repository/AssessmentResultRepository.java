package com.capstone.assessment_service.repository;

import com.capstone.assessment_service.model.AssessmentResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResultEntity, UUID> {
}
