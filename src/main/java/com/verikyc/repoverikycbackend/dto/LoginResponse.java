package com.verikyc.repoverikycbackend.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSecs,
        UserResponse user
) {
}
