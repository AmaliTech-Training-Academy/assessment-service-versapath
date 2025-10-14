package com.capstone.assessment_service.messaging;

import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopulateAssessmentEvents {
    private static final Logger logger = LoggerFactory.getLogger(PopulateAssessmentEvents.class);
    private final KafkaTemplate<String, AssessmentResultEvent> kafkaClusterTemplate;

    @Value("${ASSESSMENT_RESULT_TOPIC}")
    private String assessmentResult;

    public void populateCreateAssessmentResult(AssessmentResultEvent event){
        kafkaClusterTemplate.send(assessmentResult, event);

        logger.info("create assessment result is populated: {}", event);
    }

}
