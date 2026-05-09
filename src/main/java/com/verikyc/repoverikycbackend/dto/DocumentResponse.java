package com.verikyc.repoverikycbackend.dto;

import com.verikyc.repoverikycbackend.enums.DocumentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        DocumentStatus status,
        LocalDateTime createdAt
) {
}
