package com.capstone.assessment_service.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentResponseDto {
    private String assessmentName;
    private String assessmentType;
    private String capsuleName;
    private int maxAttempts;
    private int timeLimitMinutes;
    private int passingScore;

}
