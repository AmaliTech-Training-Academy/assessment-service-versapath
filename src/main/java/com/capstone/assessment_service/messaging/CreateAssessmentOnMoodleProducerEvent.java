package com.capstone.assessment_service.messaging;

import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAssessmentOnMoodleProducerEvent {
    @Value("${ASSESSMENT_CREATE_QUEUE}")
    private String assessmentCreateQueue;
    private static final Logger logger = LoggerFactory.getLogger(CreateAssessmentOnMoodleProducerEvent.class);
    private final RabbitTemplate rabbitTemplate;
    public void createAssessmentOnMoodle(AssessmentEvent assessmentEvent) {
        logger.info("Send command to create assessment on Moodle: {}", assessmentEvent);

        rabbitTemplate.convertAndSend(assessmentCreateQueue, assessmentEvent);
    }
}
