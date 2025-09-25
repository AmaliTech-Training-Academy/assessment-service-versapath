package com.capstone.assessment_service.messaging;

import org.common.event.SkillCapsuleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CreateCapsuleEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateCapsuleEventListener.class);

    @KafkaListener(topics = "${CAPSULE_CREATE_TOPIC}")
    public void createCapsuleEvent(SkillCapsuleEvent event) {
        logger.info("Start creating capsule event {}", event);
        // TODO: insert capsule event in the database
    }
}
