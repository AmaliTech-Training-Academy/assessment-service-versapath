package com.capstone.assessment_service.mapper;

import com.capstone.assessment_service.dto.assessment.AssessmentResponseDto;
import com.capstone.assessment_service.model.AssessmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {
    @Mapping(target = "capsuleName", source = "capsule.name")
    AssessmentResponseDto toResponseDto(AssessmentEntity entity);
}
