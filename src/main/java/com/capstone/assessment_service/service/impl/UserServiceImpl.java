package com.capstone.assessment_service.service.impl;

import com.capstone.assessment_service.exception.UserExistsException;
import com.capstone.assessment_service.model.UserSnapshot;
import com.capstone.assessment_service.repository.UserSnapshotRepository;
import com.capstone.assessment_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.common.event.ProduceUserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserSnapshotRepository userSnapshotRepository;
    @Override
    public void createUser(ProduceUserEvent userDto) {
       if(userSnapshotRepository.findById(userDto.getVersapathUserId()).isPresent()){
           throw new UserExistsException("The use provided already exists!!");
       }

        UserSnapshot userSnapshot = UserSnapshot.builder()
                .id(userDto.getVersapathUserId())
                .email(userDto.getEmail())
                .lastName(userDto.getLastName())
                .firstName(userDto.getFirstName())
                .username(userDto.getUsername())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

       userSnapshotRepository.save(userSnapshot);

       logger.info("User inserted successfully!!");
    }
}
