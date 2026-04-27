package com.verikyc.repoverikycbackend.service;

import com.verikyc.repoverikycbackend.model.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKey secretKey;

    @Value("${jwt.expiry}")
    long expiresInMS;

    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis() + expiresInMS))
                .signWith(secretKey)
                .compact();
    }
}
