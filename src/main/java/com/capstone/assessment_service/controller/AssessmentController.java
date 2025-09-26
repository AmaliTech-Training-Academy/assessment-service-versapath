package com.capstone.assessment_service.controller;

import com.capstone.assessment_service.dto.ClientResponseFormatDto;
import com.capstone.assessment_service.dto.assessment.AssessmentRequestDto;
import com.capstone.assessment_service.dto.assessment.AssessmentResponseDto;
import com.capstone.assessment_service.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("assessment")
@Tag(name = "Assessment Controller", description = "Manage all skill assessment's api")
public class AssessmentController {
    private final AssessmentService assessmentService;

    @PostMapping()
    @Operation(summary = "Create an assessment", description = "This end point creates a skill assessment metadata")
    public ResponseEntity<ClientResponseFormatDto> createAtom(
            @Valid @RequestBody AssessmentRequestDto assessmentRequestDto
    ) {
        AssessmentResponseDto savedAssessment = assessmentService.create(assessmentRequestDto);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Assessment created successfully!")
                .errors(null)
                .data(Map.of("item", savedAssessment))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
