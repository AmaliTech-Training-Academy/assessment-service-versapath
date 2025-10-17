package com.capstone.assessment_service.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentResultResponseDto {
    private String assessmentName;
    private String capsuleName;
    private String description;
    private int maxAttempts;
    private int attemptNumber;
    private int attemptLeft;
    private int passingScore;
    private double score;
    private boolean isPassed;
    private int moodleQuizId;

    public AssessmentResultResponseDto(String assessmentName, String capsuleName, String description,
                                       int maxAttempts, int attemptNumber, int attemptLeft, int passingScore,
                                       double score, boolean isPassed, int moodleQuizId) {
        this.assessmentName = assessmentName;
        this.capsuleName = capsuleName;
        this.description = description;
        this.maxAttempts = maxAttempts;
        this.attemptNumber = attemptNumber;
        this.attemptLeft = attemptLeft;
        this.passingScore = passingScore;
        this.score = score;
        this.isPassed = isPassed;
        this.moodleQuizId = moodleQuizId;
    }
}
