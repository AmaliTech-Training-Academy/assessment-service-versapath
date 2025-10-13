package com.capstone.assessment_service.controller;

import com.capstone.assessment_service.dto.ClientResponseFormatDto;
import com.capstone.assessment_service.dto.CustomPageResponse;
import com.capstone.assessment_service.dto.assessment.AssessmentRequestDto;
import com.capstone.assessment_service.dto.assessment.AssessmentResponseDto;
import com.capstone.assessment_service.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("assessment")
@Tag(name = "Assessment Controller", description = "Manage all skill assessment's api")
public class AssessmentController {
    private final AssessmentService assessmentService;

    @PostMapping()
    @Operation(summary = "Create an assessment", description = "This end point creates a skill assessment metadata")
    public ResponseEntity<ClientResponseFormatDto> createAssessment(
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

    @GetMapping()
    @Operation(summary = "Retrieve assessments", description = "This end point allows the fetching of all assessments")
    public ResponseEntity<ClientResponseFormatDto> fetchAllAssessments(Pageable pageable) {
        CustomPageResponse<AssessmentResponseDto> assessments = this.assessmentService.findAll(pageable);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Fetch all assessments")
                .errors(null)
                .data(assessments)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter assessments", description = "This end point allows the filtering assessments by name")
    public ResponseEntity<ClientResponseFormatDto> filterAssessments(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "${PAGE_SIZE}") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        CustomPageResponse<AssessmentResponseDto> assessments = this.assessmentService.filter(name, pageable);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Filter assessments")
                .errors(null)
                .data(assessments)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{capsuleId}/capsule")
    @Operation(summary = "Retrieve assessments by capsule", description = "This end point allows the fetching of all assessments by capsule")
    public ResponseEntity<ClientResponseFormatDto> fetchAssessmentsByCapsule(@PathVariable UUID capsuleId) {
        List<AssessmentResponseDto> assessments = this.assessmentService.findByCapsuleId(capsuleId);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Fetch all assessments by capsule")
                .errors(null)
                .data(assessments)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
