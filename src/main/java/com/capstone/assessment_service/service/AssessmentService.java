package com.capstone.assessment_service.service;

import com.capstone.assessment_service.dto.CustomPageResponse;
import com.capstone.assessment_service.dto.assessment.AssessmentRequestDto;
import com.capstone.assessment_service.dto.assessment.AssessmentResponseDto;
import com.capstone.assessment_service.model.AssessmentEntity;
import org.common.event.AssessmentUpdateEvent;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AssessmentService {
    AssessmentResponseDto create(AssessmentRequestDto dto);
    Optional<AssessmentEntity> findByName(String name);
    void updateAssessmentWithMoodleData(AssessmentUpdateEvent assessmentUpdateEvent);
    CustomPageResponse<AssessmentResponseDto> findAll(Pageable pageable);
    CustomPageResponse<AssessmentResponseDto> filter(String name, Pageable pageable);
}
