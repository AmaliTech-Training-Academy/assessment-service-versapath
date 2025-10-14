package com.capstone.assessment_service.dto.assessment;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentUpdateRequestDto {
    @Size(min = 3, message = "Assessment name must have at least 3 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Assessment name cannot contain numbers")
    private String assessmentName;

    private String assessmentType;

    @Min(value = 1, message = "Max attempts Must be at least 1")
    @Max(value = 100, message = "Max attempts Must not exceed 100")
    private int maxAttempts;

    @Min(value = 1, message = "Time limit Must be at least 1")
    @Max(value = 1000, message = "Time limit Must not exceed 1000")
    private int timeLimitMinutes;

    @Min(value = 1, message = "Passing Score Must be at least 1")
    @Max(value = 100, message = "Passing Score Must not exceed 100")
    private int passingScore;

    private String instructions;
    private String status;
    private String description;
    private UUID skillCapsuleId;
}
