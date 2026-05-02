package com.verikyc.repoverikycbackend.service;

import com.verikyc.repoverikycbackend.exception.UserNotFoundException;
import com.verikyc.repoverikycbackend.model.entity.UserEntity;
import com.verikyc.repoverikycbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity findUserByEmail(String email){
        // check if the email exist in db else throw error
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if(userEntity.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return userEntity.get();
    }
}
