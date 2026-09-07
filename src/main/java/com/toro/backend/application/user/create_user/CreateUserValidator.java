package com.toro.backend.application.user.create_user;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.repository.UserRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;
import com.toro.backend.presentation.user.request.CreateUserRequest;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class CreateUserValidator {

    private final UserRepository userRepository;


    public void validate(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessValidationException("Email already exists.");
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BusinessValidationException("Phone number already exists.");
        }
    }

}
