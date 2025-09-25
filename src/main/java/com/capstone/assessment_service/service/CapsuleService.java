package com.capstone.assessment_service.service;

import com.capstone.assessment_service.model.SkillCapsuleEntity;
import org.common.event.SkillCapsuleEvent;

import java.util.Optional;

public interface CapsuleService {
    void create(SkillCapsuleEvent capsuleEvent);
    Optional<SkillCapsuleEntity> findByName(String name);
}
