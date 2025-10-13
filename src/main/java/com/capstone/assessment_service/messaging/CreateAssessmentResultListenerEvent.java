package com.capstone.assessment_service.messaging;

import com.capstone.assessment_service.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAssessmentResultListenerEvent {
    private static final Logger logger = LoggerFactory.getLogger(CreateAssessmentResultListenerEvent.class);
    private final AssessmentService assessmentService;

    @RabbitListener(queues = "${ASSESSMENT_RESULT_QUEUE}")
    public void handleMoodleUserCreation(AssessmentResultEvent event) {
        logger.info("Start inserting assessment result to the database: {}", event);

        assessmentService.createAssessmentResult(event); // insert assessment result into DB
    }
}
