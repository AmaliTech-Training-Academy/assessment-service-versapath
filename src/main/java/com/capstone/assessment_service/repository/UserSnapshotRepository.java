package com.capstone.assessment_service.repository;

import com.capstone.assessment_service.model.UserSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSnapshotRepository extends JpaRepository<UserSnapshot, UUID> {

}
