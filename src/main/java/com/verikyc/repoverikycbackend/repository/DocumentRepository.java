package com.verikyc.repoverikycbackend.repository;

import com.verikyc.repoverikycbackend.model.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID>{

}
