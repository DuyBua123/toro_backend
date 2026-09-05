package com.toro.backend.application.authentication.login;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.RevokedReason;
import com.toro.backend.infrastructure.database.models.LoginSession;
import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.LoginSessionRepository;
import com.toro.backend.infrastructure.security.jwt.JwtService;
import com.toro.backend.presentation.authentication.request.LoginRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final LoginValidator validator;
    private final JwtService jwtService;
    private final LoginSessionRepository loginSessionRepository;


    @Transactional
    public LoginResult execute(LoginRequest request) {

        // Business validation
        User user = validator.validate(request);

        Instant accessTokenExpiresAt =
            jwtService.generateAccessTokenExpiresAt();

        Instant refreshTokenExpiresAt =
            jwtService.generateRefreshTokenExpiresAt();

        String accessToken = jwtService.generateAccessToken(
                user,
                Instant.now(),
                accessTokenExpiresAt
        );

        String refreshToken = jwtService.generateRefreshToken();
        String hashedRefreshToken = jwtService.hashRefreshToken(refreshToken);

        // Create / rotate login session
        createLoginSession(user, hashedRefreshToken, accessTokenExpiresAt, refreshTokenExpiresAt);

        
        return new LoginResult(
                accessToken,
                refreshToken,
                refreshTokenExpiresAt
        );
    }


    // PRIVATE METHODS
    private LoginSession createLoginSession(
            User user,
            String hashedRefreshToken,
            Instant accessExpiresAt,
            Instant refreshExpiresAt
    ) {

        LoginSession loginSession = loginSessionRepository
                .findFirstByUserIdAndRevokedReasonIsNull(user.getId())
                .map(previousSession -> {

                        previousSession.setHashedRefreshToken(hashedRefreshToken);
                        previousSession.setAccessExpiresAt(accessExpiresAt);
                        previousSession.setRefreshExpiresAt(refreshExpiresAt);

                        loginSessionRepository.save(previousSession);

                        return previousSession;
                })
                .orElseGet(() -> {
                        LoginSession newSession = new LoginSession();
                        
                        newSession.setUser(user);
                        newSession.setHashedRefreshToken(hashedRefreshToken);
                        newSession.setAccessExpiresAt(accessExpiresAt);
                        newSession.setRefreshExpiresAt(refreshExpiresAt);
                        newSession.setRevokedReason(null);
                        newSession.setRevokedAt(null);

                        loginSessionRepository.save(newSession);

                        return newSession;
                });

                return loginSession;
    }

}
