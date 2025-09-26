package com.capstone.assessment_service.service.impl;

import com.capstone.assessment_service.dto.assessment.AssessmentRequestDto;
import com.capstone.assessment_service.dto.assessment.AssessmentResponseDto;
import com.capstone.assessment_service.exception.CapsuleExistsException;
import com.capstone.assessment_service.exception.CapsuleNotFoundException;
import com.capstone.assessment_service.mapper.AssessmentMapper;
import com.capstone.assessment_service.model.AssessmentEntity;
import com.capstone.assessment_service.model.SkillCapsuleEntity;
import com.capstone.assessment_service.repository.AssessmentRepository;
import com.capstone.assessment_service.service.AssessmentService;
import com.capstone.assessment_service.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentServiceImpl.class);
    private final AssessmentRepository assessmentRepository;
    private final CapsuleService capsuleService;
    private final AssessmentMapper assessmentMapper;

    @Override
    public AssessmentResponseDto create(AssessmentRequestDto dto) {
        if(findByName(dto.getAssessmentName()).isPresent()){
            throw new CapsuleExistsException(
                    String.format("An assessment with the name '%s' already exist",
                            dto.getAssessmentName()));
        }
        SkillCapsuleEntity capsule = capsuleService.findById(dto.getSkillCapsuleId())
                .orElseThrow( () -> new CapsuleNotFoundException("A skill capsule provided doesn't exist")
                );
        AssessmentEntity assessment = assessmentMapper.toEntity(dto);
        assessment.setCapsule(capsule);
        assessment.setCreatedAt(LocalDateTime.now());
        assessment.setUpdatedAt(LocalDateTime.now());

        AssessmentEntity savedAssessment = assessmentRepository.save(assessment);
        logger.info("Assessment inserted successfully");

        return assessmentMapper.toResponseDto(savedAssessment);
    }

    @Override
    public Optional<AssessmentEntity> findByName(String name) {
        return assessmentRepository.findByAssessmentName(name);
    }
}
