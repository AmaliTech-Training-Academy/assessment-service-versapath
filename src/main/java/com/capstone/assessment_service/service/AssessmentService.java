package com.capstone.assessment_service.service;

import com.capstone.assessment_service.dto.CustomPageResponse;
import com.capstone.assessment_service.dto.assessment.AssessmentRequestDto;
import com.capstone.assessment_service.dto.assessment.AssessmentResponseDto;
import com.capstone.assessment_service.model.AssessmentEntity;
import org.common.event.AssessmentUpdateEvent;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssessmentService {
    AssessmentResponseDto create(AssessmentRequestDto dto);
    Optional<AssessmentEntity> findByName(String name);
    void updateAssessmentWithMoodleData(AssessmentUpdateEvent assessmentUpdateEvent);
    CustomPageResponse<AssessmentResponseDto> findAll(Pageable pageable);
    CustomPageResponse<AssessmentResponseDto> filter(String name, Pageable pageable);
    List<AssessmentResponseDto> findByCapsuleId(UUID capsuleId);
}
