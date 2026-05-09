package com.verikyc.repoverikycbackend.repository;

import com.verikyc.repoverikycbackend.model.entity.VerificationResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationResultRepository extends JpaRepository<VerificationResultEntity, UUID> {
    Optional<VerificationResultEntity> findByDocumentId(UUID documentId);
}
