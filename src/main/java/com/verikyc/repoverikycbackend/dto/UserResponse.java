package com.verikyc.repoverikycbackend.dto;

import com.verikyc.repoverikycbackend.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
