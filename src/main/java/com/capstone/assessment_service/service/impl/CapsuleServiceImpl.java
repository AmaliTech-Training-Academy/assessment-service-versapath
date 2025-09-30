package com.capstone.assessment_service.service.impl;

import com.capstone.assessment_service.exception.CapsuleExistsException;
import com.capstone.assessment_service.model.SkillCapsuleEntity;
import com.capstone.assessment_service.repository.CapsuleRepository;
import com.capstone.assessment_service.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.common.event.SkillCapsuleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CapsuleServiceImpl implements CapsuleService {
    private static final Logger logger = LoggerFactory.getLogger(CapsuleServiceImpl.class);
    private final CapsuleRepository capsuleRepository;

    @Override
    public void create(SkillCapsuleEvent capsuleEvent) {

        SkillCapsuleEntity capsule = SkillCapsuleEntity.builder()
                .id(capsuleEvent.getId())
                .name(capsuleEvent.getName())
                .description(capsuleEvent.getDescription())
                .proficiencyLevel(capsuleEvent.getProficiencyLevel())
                .difficulty(capsuleEvent.getDifficulty())
                .moodleCourseId(capsuleEvent.getMoodleCourseId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        try{
            capsuleRepository.save(capsule);

            logger.info("Capsule inserted successfully!");
        }catch (DataIntegrityViolationException e){
            throw new CapsuleExistsException(
                    String.format("A skill capsule with the name '%s' already exist",
                            capsuleEvent.getName()));
        }

    }

    @Override
    public Optional<SkillCapsuleEntity> findByName(String name) {
        return this.capsuleRepository.findByName(name);
    }

    public Optional<SkillCapsuleEntity> findById(UUID capsuleId){
        return capsuleRepository.findById(capsuleId);
    }
}
