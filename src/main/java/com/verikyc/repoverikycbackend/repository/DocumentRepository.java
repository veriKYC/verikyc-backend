package com.verikyc.repoverikycbackend.repository;

import com.verikyc.repoverikycbackend.model.entity.DocumentEntity;
import com.verikyc.repoverikycbackend.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID>{
    Optional<DocumentEntity> findByIdAndUser(UUID id, UserEntity user);
    Page<DocumentEntity> findAllByUser(UserEntity user, Pageable pageable);

    UUID user(UserEntity user);
}
