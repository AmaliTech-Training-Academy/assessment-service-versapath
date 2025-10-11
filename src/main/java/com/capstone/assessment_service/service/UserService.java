package com.capstone.assessment_service.service;

import org.common.event.ProduceUserEvent;

public interface UserService {
    void createUser(ProduceUserEvent userDto);
}
