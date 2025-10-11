package com.capstone.assessment_service.messaging;

import com.capstone.assessment_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.common.event.ProduceUserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserListener.class);
    private final UserService userService;
    @KafkaListener(topics = "${USER_CREATE_TOPIC}")
    public void createUser(ProduceUserEvent event) {
        logger.info("Start creating user event {}", event);

        userService.createUser(event); // store user info in the database
    }
}
