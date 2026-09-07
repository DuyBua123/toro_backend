package com.toro.backend.application.user.create_user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.AppRole;
import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.UserRepository;
import com.toro.backend.presentation.user.request.CreateUserRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CreateUserUseCase {

    private final CreateUserValidator createUserValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public CreateUserResult execute(CreateUserRequest request) {
        createUserValidator.validate(request);

        User user = User.builder()
            .fullName(request.fullName())
            .email(request.email())
            .phoneNumber(request.phoneNumber())
            .passwordHash(passwordEncoder.encode(request.password()))
            .isActive(request.isActive())
            .role(AppRole.USER)
            .build();

        User savedUser = userRepository.save(user);

        return toResult(savedUser);
    }


    // PRIVATE METHODS
    private CreateUserResult toResult(User user) {
        return new CreateUserResult(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.isActive(),
            user.getCreatedAt()
        );
    }

}
