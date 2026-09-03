package com.toro.backend.application.authentication.login;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.UserRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;
import com.toro.backend.presentation.authentication.request.LoginRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User validate(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->  new BusinessValidationException(
                        "Invalid username or password."
                    ));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new BusinessValidationException(
                "Invalid username or password."
            );
        }

        return user;
    }

}
