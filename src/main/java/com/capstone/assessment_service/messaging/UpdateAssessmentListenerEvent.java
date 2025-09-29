package com.capstone.assessment_service.messaging;

import com.capstone.assessment_service.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateAssessmentListenerEvent {
    private static final Logger logger = LoggerFactory.getLogger(UpdateAssessmentListenerEvent.class);
    private final AssessmentService assessmentService;

    @RabbitListener(queues = "${ASSESSMENT_UPDATE_QUEUE}")
    public void handleMoodleUserCreation(AssessmentUpdateEvent assessmentUpdateEvent) {
        logger.info("Start assessment with Moodle data: {}", assessmentUpdateEvent);

        assessmentService.updateAssessmentWithMoodleData(assessmentUpdateEvent);

    }
}
