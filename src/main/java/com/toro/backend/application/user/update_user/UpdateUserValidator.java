package com.toro.backend.application.user.update_user;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.UserRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;
import com.toro.backend.presentation.user.request.UpdateUserRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateUserValidator {

    private final UserRepository userRepository;


    public User validate(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessValidationException("User not found."));

        if (userRepository.existsByEmailAndIdNot(request.email(), userId)) {
            throw new BusinessValidationException("Email already exists.");
        }

        if (userRepository.existsByPhoneNumberAndIdNot(request.phoneNumber(), userId)) {
            throw new BusinessValidationException("Phone number already exists.");
        }

        return user;
    }

}
