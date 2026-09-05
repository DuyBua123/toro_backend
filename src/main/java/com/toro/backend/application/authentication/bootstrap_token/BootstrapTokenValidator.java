package com.toro.backend.application.authentication.bootstrap_token;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.LoginSession;
import com.toro.backend.infrastructure.database.repository.LoginSessionRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;
import com.toro.backend.infrastructure.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class BootstrapTokenValidator {

    private final JwtService jwtService;
    private final LoginSessionRepository loginSessionRepository;


    public LoginSession validate(String refreshToken) {

        // 1. Basic validation
        if (refreshToken == null || refreshToken.isBlank()) {
            System.out.println("Invalid refresh token: Token is missing");
            throw new BusinessValidationException("Invalid refresh token");
        }

        // 2. Hash the provided opaque token
        String hashedRefreshToken;

        try {
            hashedRefreshToken = jwtService.hashRefreshToken(refreshToken);
        } catch (Exception e) {
            System.out.println("Failed to hash refresh token: " + e.getMessage());
            throw new BusinessValidationException("Invalid refresh token");
        }

        // 3. Find session by hashed refresh token
        LoginSession session =
            loginSessionRepository.findByHashedRefreshToken(hashedRefreshToken)
            .orElseThrow(() -> {
                System.out.println("Invalid refresh token: Session not found");
                throw new BusinessValidationException("Invalid refresh token");
            });


        // 4. Verify user still exists
        if (session.getUser() == null) {
            System.out.println("Invalid refresh token: User not found");
            throw new BusinessValidationException("Invalid refresh token");
        }

        // 5. Verify session isn't revoked
        if (session.getRevokedAt() != null) {
            System.out.println("Invalid refresh token: Session revoked");
            throw new BusinessValidationException("Invalid refresh token");
        }


        // 6. Verify refresh session expiration
        Instant refreshExpiresAt = session.getRefreshExpiresAt();

        if (!refreshExpiresAt.isAfter(Instant.now())) {
            System.out.println("Invalid refresh token:: Login Session expired");
            throw new BusinessValidationException("Invalid refresh token");
        }


        return session;
    } 

}
