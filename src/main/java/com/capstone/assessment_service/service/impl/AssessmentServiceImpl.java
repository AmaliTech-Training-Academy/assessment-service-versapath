package com.capstone.assessment_service.service.impl;

import com.capstone.assessment_service.dto.CustomPageResponse;
import com.capstone.assessment_service.dto.PaginationData;
import com.capstone.assessment_service.dto.assessment.AssessmentRequestDto;
import com.capstone.assessment_service.dto.assessment.AssessmentResponseDto;
import com.capstone.assessment_service.dto.assessment.AssessmentResultResponseDto;
import com.capstone.assessment_service.dto.assessment.AssessmentUpdateRequestDto;
import com.capstone.assessment_service.exception.AssessmentExistsException;
import com.capstone.assessment_service.exception.CapsuleNotFoundException;
import com.capstone.assessment_service.exception.UserNotFoundException;
import com.capstone.assessment_service.mapper.AssessmentMapper;
import com.capstone.assessment_service.messaging.CreateAssessmentOnMoodleProducerEvent;
import com.capstone.assessment_service.messaging.PopulateAssessmentEvents;
import com.capstone.assessment_service.model.AssessmentEntity;
import com.capstone.assessment_service.model.AssessmentResultEntity;
import com.capstone.assessment_service.model.SkillCapsuleEntity;
import com.capstone.assessment_service.model.UserSnapshot;
import com.capstone.assessment_service.repository.AssessmentRepository;
import com.capstone.assessment_service.repository.AssessmentResultRepository;
import com.capstone.assessment_service.repository.CapsuleRepository;
import com.capstone.assessment_service.repository.UserSnapshotRepository;
import com.capstone.assessment_service.service.AssessmentService;
import com.capstone.assessment_service.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentEvent;
import org.common.event.AssessmentResultEvent;
import org.common.event.AssessmentUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentServiceImpl.class);
    private final AssessmentRepository assessmentRepository;
    private final CapsuleService capsuleService;
    private final AssessmentMapper assessmentMapper;
    private final CreateAssessmentOnMoodleProducerEvent createAssessmentOnMoodleProducerEvent;
    private final AssessmentResultRepository assessmentResultRepository;
    private final UserSnapshotRepository userSnapshotRepository;
    private final PopulateAssessmentEvents populateAssessmentEvents;
    private final CapsuleRepository capsuleRepository;

    @Override
    public AssessmentResponseDto create(AssessmentRequestDto dto) {

        SkillCapsuleEntity capsule = capsuleService.findById(dto.getSkillCapsuleId())
                .orElseThrow( () -> new CapsuleNotFoundException("A skill capsule provided doesn't exist")
                );
        AssessmentEntity assessment = assessmentMapper.toEntity(dto);
        assessment.setCapsule(capsule);
        assessment.setCreatedAt(LocalDateTime.now());
        assessment.setUpdatedAt(LocalDateTime.now());

        try {

            AssessmentEntity savedAssessment = assessmentRepository.save(assessment);
            logger.info("Assessment inserted successfully");

            sendCommandToCreateAssessmentOnMoodle(savedAssessment);

            return assessmentMapper.toResponseDto(savedAssessment);

        } catch (DataIntegrityViolationException e) {

            throw new AssessmentExistsException(
                    String.format("An assessment with the name '%s' already exists", dto.getAssessmentName()));
        }

    }

    @Override
    public Optional<AssessmentEntity> findByName(String name) {
        return assessmentRepository.findByAssessmentName(name);
    }

    @Override
    public void updateAssessmentWithMoodleData(AssessmentUpdateEvent assessmentUpdateEvent) {
        AssessmentEntity assessment = assessmentRepository.findByAssessmentName(assessmentUpdateEvent.getAssessmentName())
                .orElseThrow( () -> new CapsuleNotFoundException("An assessment provided doesn't exist")
                );
        assessment.setMoodleCourseModuleId(assessmentUpdateEvent.getMoodleCourseModuleId());
        assessment.setMoodleQuizId(assessmentUpdateEvent.getQuizId());

        assessmentRepository.save(assessment);

        logger.info("Assessment updated successfully {}", assessment.getAssessmentName());
    }

    @Override
    public CustomPageResponse<AssessmentResponseDto> findAll(Pageable pageable) {
        Page<AssessmentEntity> assessmentList = assessmentRepository.findAll(pageable);
        Page<AssessmentResponseDto> assessments = assessmentList.map(assessmentMapper::toResponseDto);

        logger.info("Assessment fetched successfully");

        return CustomPageResponse.<AssessmentResponseDto>builder()
                .items(assessments.getContent())
                .pagination(PaginationData.builder()
                        .page(assessments.getNumber())
                        .size(assessments.getSize())
                        .totalElements(assessments.getTotalElements())
                        .totalPages(assessments.getTotalPages())
                        .hasNext(assessments.hasNext())
                        .hasPrevious(assessments.hasPrevious())
                        .build())
                .build();
    }

    @Override
    public CustomPageResponse<AssessmentResponseDto> filter(String name, Pageable pageable) {

        Page<AssessmentEntity> assessmentList= null;
        // if assessment name isn't provided fetch 20 first items
        if(name == null || name.trim().isEmpty()){
            assessmentList = this.assessmentRepository.findAll(PageRequest.of(0, 20));
        }else{
            assessmentList = this.assessmentRepository.findByAssessmentNameContainingIgnoreCase(name, pageable);
        }

        Page<AssessmentResponseDto> assessments = assessmentList.map(assessmentMapper::toResponseDto);

        logger.info("Assessment filtered successfully");

        return CustomPageResponse.<AssessmentResponseDto>builder()
                .items(assessments.getContent())
                .pagination(PaginationData.builder()
                        .page(assessments.getNumber())
                        .size(assessments.getSize())
                        .totalElements(assessments.getTotalElements())
                        .totalPages(assessments.getTotalPages())
                        .hasNext(assessments.hasNext())
                        .hasPrevious(assessments.hasPrevious())
                        .build())
                .build();
    }

    @Override
    public List<AssessmentResponseDto> findByCapsuleId(UUID capsuleId) {
        List<AssessmentEntity> assessmentList = assessmentRepository.findByCapsuleId(capsuleId);
        return assessmentList.stream().map(assessmentMapper::toResponseDto).toList();
    }

    public void createAssessmentResult(AssessmentResultEvent event) {
        AssessmentEntity assessment = assessmentRepository.findByMoodleQuizId(event.getMoodleQuizId())
                .orElseThrow( () -> new AssessmentExistsException("Assessment provided doesn't exist")
                );

        UserSnapshot leaner = userSnapshotRepository.findById(event.getUserId())
                .orElseThrow( () -> new UserNotFoundException("User provided doesn't exist")
                );

        AssessmentResultEntity assessmentResult = AssessmentResultEntity.builder()
                .assessment(assessment)
                .learner(leaner)
                .attemptNumber(event.getAttemptNumber())
                .score(event.getGrade())
                .isPassed(event.getGrade() >= assessment.getPassingScore())
                .startedAt(event.getTimeStart())
                .submittedAt(event.getTimeFinish())
                .build();


        AssessmentResultEntity assessmentResultEntity = assessmentResultRepository.save(assessmentResult);

        populateAssessmentResult(assessmentResultEntity);

        logger.info("Assessment result inserted successfully");
    }

    @Override
    public void deleteById(UUID id) {
        if(assessmentRepository.findById(id).isEmpty()){
            throw new AssessmentExistsException("Assessment provided doesn't exist");
        }

        assessmentRepository.deleteById(id);

        logger.info("Assessment deleted successfully");
    }

    @Override
    public AssessmentResponseDto partialUpdate(AssessmentUpdateRequestDto dto, UUID id) {
        AssessmentEntity assessment = assessmentRepository.findById(id)
                .orElseThrow(()-> new AssessmentExistsException("Assessment provided doesn't exist"));

        if(dto.getAssessmentType() != null){
            assessment.setAssessmentType(dto.getAssessmentType());
        }

        if(dto.getAssessmentName() != null){
            assessment.setAssessmentName(dto.getAssessmentName());
        }

        if(dto.getDescription() != null){
            assessment.setDescription(dto.getDescription());
        }

        if(dto.getPassingScore() != assessment.getPassingScore()){
            assessment.setPassingScore(dto.getPassingScore());
        }

        if(dto.getMaxAttempts() != assessment.getMaxAttempts()){
            assessment.setMaxAttempts(dto.getMaxAttempts());
        }

        if(dto.getInstructions() != null){
            assessment.setDescription(dto.getDescription());
        }

        if(dto.getTimeLimitMinutes() != assessment.getTimeLimitMinutes()){
            assessment.setTimeLimitMinutes(dto.getTimeLimitMinutes());
        }

        if(dto.getStatus() != null){
            assessment.setStatus(dto.getStatus());
        }

        if(dto.getSkillCapsuleId() != null){
            SkillCapsuleEntity capsule = capsuleRepository.findById(dto.getSkillCapsuleId())
                    .orElseThrow(()-> new CapsuleNotFoundException("Capsule provided doesn't exist"));

            assessment.setCapsule(capsule);
        }

        logger.info("Assessment updated successfully");

        return assessmentMapper.toResponseDto(assessmentRepository.save(assessment));
    }

    @Override
    public List<AssessmentResultResponseDto> findAssessmentWithLearnerResult(UUID capsuleId) {
        SkillCapsuleEntity capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(()-> new CapsuleNotFoundException("The Capsule provided doesn't exist"));

        // get the current authenticated user
        String stringUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId =  UUID.fromString(stringUserId);

        UserSnapshot learner = userSnapshotRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The learner provided "));

        return null;
    }

    void sendCommandToCreateAssessmentOnMoodle(AssessmentEntity savedAssessment){
        AssessmentEvent assessmentEvent = AssessmentEvent.builder()
                .assessmentName(savedAssessment.getAssessmentName())
                .assessmentType(savedAssessment.getAssessmentType())
                .maxAttempts(savedAssessment.getMaxAttempts())
                .passingScore(savedAssessment.getPassingScore())
                .timeLimitMinutes(savedAssessment.getTimeLimitMinutes())
                .capsuleId(savedAssessment.getCapsule().getId())
                .moodleCourseId(savedAssessment.getCapsule().getMoodleCourseId())
                .capsuleId(savedAssessment.getCapsule().getId())
                .build();

        createAssessmentOnMoodleProducerEvent.createAssessmentOnMoodle(assessmentEvent);
    }

    private void populateAssessmentResult(AssessmentResultEntity assessmentResult ){
        AssessmentResultEvent assessmentResultEvent = AssessmentResultEvent.builder()
                .userId(assessmentResult.getLearner().getId())
                .assessmentName(assessmentResult.getAssessment().getAssessmentName())
                .assessmentId(assessmentResult.getAssessment().getId())
                .timeStart(assessmentResult.getStartedAt())
                .timeFinish(assessmentResult.getSubmittedAt())
                .moodleQuizId(assessmentResult.getAssessment().getMoodleQuizId())
                .attemptNumber(assessmentResult.getAttemptNumber())
                .grade(assessmentResult.getScore())
                .capsuleId(assessmentResult.getAssessment().getCapsule().getId())
                .build();

        populateAssessmentEvents.populateCreateAssessmentResult(assessmentResultEvent);

        logger.info("Assessment result populated successfully {}", assessmentResultEvent);

    }
}
