package com.verikyc.repoverikycbackend.service;

import com.verikyc.repoverikycbackend.dto.LoginRequest;
import com.verikyc.repoverikycbackend.dto.LoginResponse;
import com.verikyc.repoverikycbackend.dto.UserResponse;
import com.verikyc.repoverikycbackend.enums.Role;
import com.verikyc.repoverikycbackend.exception.EmailAlreadyExistsException;
import com.verikyc.repoverikycbackend.exception.InvalidCredentialsException;
import com.verikyc.repoverikycbackend.model.entity.UserEntity;
import com.verikyc.repoverikycbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${jwt.expiry}")
    private long expiresInMilliSecs;

    public UserResponse registerUser(String email, String password) {

        // todo 1. check if the email exist or not
        Optional<UserEntity> entity = userRepository.findByEmail(email);
        if(entity.isPresent()){
            throw new EmailAlreadyExistsException("User with email " + email + " already exists");
        }

        // todo 2. if email doesn't exist populate User entity object
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPasswordHash(passwordEncoder.encode(password)); // need to do password hashing
        userEntity.setRole(Role.USER);

        // todo 3. save it
        UserEntity savedEntity = userRepository.save(userEntity);

        // todo 4. build UserResponse from newly saved entity

        return new UserResponse(savedEntity.getId(), savedEntity.getEmail(), savedEntity.getRole(), savedEntity.getCreatedAt());

    }

    public LoginResponse userLogin(LoginRequest request){

        // TODO - check if the email is not registered, throw exception if not found
        // TODO - EmailNotFoundException(message) to be built
        Optional<UserEntity> entity = userRepository.findByEmail(request.email());

        if(entity.isEmpty()){
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        UserEntity userEntity = entity.get();
        // TODO - CHECK IF THE PASSWORD MATCHES
        if(!passwordEncoder.matches(request.password(), userEntity.getPasswordHash())){
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        // TODO - GENERATE JWT FOR THE USER and RETURN AS LOGIN RESPONSE
        return new LoginResponse(
                jwtService.generateToken(userEntity),
                "Bearer",
                expiresInMilliSecs/1000,
                new UserResponse(userEntity.getId(), userEntity.getEmail(), userEntity.getRole(), userEntity.getCreatedAt())
        );
    }

}
