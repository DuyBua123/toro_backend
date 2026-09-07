package com.toro.backend.application.user.get_user;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.UserRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;

import lombok.RequiredArgsConstructor;


@Component 
@RequiredArgsConstructor 
public class GetUserValidator {

    private final UserRepository userRepository;


    public User validate(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessValidationException("User not found."));
    }

}
