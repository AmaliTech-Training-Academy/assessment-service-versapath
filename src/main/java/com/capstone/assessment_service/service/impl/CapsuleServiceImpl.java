package com.capstone.assessment_service.service.impl;

import com.capstone.assessment_service.exception.CapsuleExistsException;
import com.capstone.assessment_service.model.SkillCapsuleEntity;
import com.capstone.assessment_service.repository.CapsuleRepository;
import com.capstone.assessment_service.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.common.event.SkillCapsuleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CapsuleServiceImpl implements CapsuleService {
    private static final Logger logger = LoggerFactory.getLogger(CapsuleServiceImpl.class);
    private final CapsuleRepository capsuleRepository;

    @Override
    public void create(SkillCapsuleEvent capsuleEvent) {
        if(findByName(capsuleEvent.getName()).isPresent()){
            throw new CapsuleExistsException(
                    String.format("A Skill Capsule with the name '%s' already exist",
                            capsuleEvent.getName()));
        }
        //TODO: insert capsule into DB

    }

    @Override
    public Optional<SkillCapsuleEntity> findByName(String name) {
        return this.capsuleRepository.findByName(name);
    }
}
