package com.verikyc.repoverikycbackend.controller;

import com.verikyc.repoverikycbackend.dto.LoginRequest;
import com.verikyc.repoverikycbackend.dto.LoginResponse;
import com.verikyc.repoverikycbackend.dto.RegisterRequest;
import com.verikyc.repoverikycbackend.dto.UserResponse;
import com.verikyc.repoverikycbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(registerRequest.email(), registerRequest.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.userLogin(loginRequest));
    }

}
