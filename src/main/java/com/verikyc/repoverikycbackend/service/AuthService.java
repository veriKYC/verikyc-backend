package com.verikyc.repoverikycbackend.service;

import com.verikyc.repoverikycbackend.dto.UserResponse;
import com.verikyc.repoverikycbackend.enums.Role;
import com.verikyc.repoverikycbackend.exception.EmailAlreadyExistsException;
import com.verikyc.repoverikycbackend.model.entity.UserEntity;
import com.verikyc.repoverikycbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(String email, String password) {

        // todo 1. check if the email exist or not
        Optional<UserEntity> entity = userRepository.findByEmail(email);
        if(entity.isPresent()){
            throw new EmailAlreadyExistsException("User with email " + email + " already exists");
        }

        // todo 2. if email doesn't exist build populate User entity
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPasswordHash(passwordEncoder.encode(password)); // need to do password hashing
        userEntity.setRole(Role.USER);

        // todo 3. save it
        UserEntity savedEntity = userRepository.save(userEntity);

        // todo 4. build UserResponse from newly saved entity

        return new UserResponse(savedEntity.getId(), savedEntity.getEmail(), savedEntity.getRole(), savedEntity.getCreatedAt());

    }
}
