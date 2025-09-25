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
public class AssessmentRequestDto {
    @NotBlank(message = "Assessment name is required")
    @Size(min = 3, message = "Assessment name must have at least 3 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Assessment name cannot contain numbers")
    private String assessmentName;

    private String assessmentType;

    @Min(value = 1, message = "Max attempts Must be at least 1 hour")
    @Max(value = 1000, message = "Max attempts Must not exceed 1000 hours")
    private int maxAttempts;

    @Min(value = 1, message = "Time limit Must be at least 1 hour")
    @Max(value = 1000, message = "Time limit Must not exceed 1000 hours")
    private int timeLimitMinutes;

    @Min(value = 1, message = "Passing Score Must be at least 1 hour")
    @Max(value = 1000, message = "Passing Score Must not exceed 1000 hours")
    private int passingScore;

    private String instructions;
    private UUID skillCapsuleId;
}
