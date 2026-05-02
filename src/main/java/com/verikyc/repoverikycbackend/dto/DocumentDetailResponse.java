package com.verikyc.repoverikycbackend.dto;

import com.verikyc.repoverikycbackend.enums.DocumentStatus;
import com.verikyc.repoverikycbackend.enums.DocumentType;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentDetailResponse(
    UUID id,
    DocumentStatus status,
    DocumentType documentType,
    String imageUrl,
    String selfieUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
