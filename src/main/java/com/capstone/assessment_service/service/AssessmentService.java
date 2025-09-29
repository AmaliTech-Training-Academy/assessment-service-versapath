package com.capstone.assessment_service.service;

import com.capstone.assessment_service.dto.assessment.AssessmentRequestDto;
import com.capstone.assessment_service.dto.assessment.AssessmentResponseDto;
import com.capstone.assessment_service.model.AssessmentEntity;
import org.common.event.AssessmentUpdateEvent;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AssessmentService {
    AssessmentResponseDto create(AssessmentRequestDto dto);
    Optional<AssessmentEntity> findByName(String name);
    void updateAssessmentWithMoodleData(AssessmentUpdateEvent assessmentUpdateEvent);
}
