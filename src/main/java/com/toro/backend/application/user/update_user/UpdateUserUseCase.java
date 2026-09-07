package com.toro.backend.application.user.update_user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.UserRepository;
import com.toro.backend.presentation.user.request.UpdateUserRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UpdateUserValidator updateUserValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UpdateUserResult execute(Long userId, UpdateUserRequest request) {
        User user = updateUserValidator.validate(userId, request);

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setActive(request.isActive());

        User savedUser = userRepository.save(user);

        return toResult(savedUser);
    }


    // PRIVATE METHODS
    private UpdateUserResult toResult(User user) {
        return new UpdateUserResult(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.isActive(),
            user.getCreatedAt()
        );
    }

}
