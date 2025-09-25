package com.capstone.assessment_service.messaging;

import com.capstone.assessment_service.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.common.event.SkillCapsuleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCapsuleEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateCapsuleEventListener.class);
    private final CapsuleService capsuleService;

    @KafkaListener(topics = "${CAPSULE_CREATE_TOPIC}")
    public void createCapsuleEvent(SkillCapsuleEvent event) {
        logger.info("Start creating capsule event {}", event);

        capsuleService.create(event); // insert capsule into the DB
    }
}
