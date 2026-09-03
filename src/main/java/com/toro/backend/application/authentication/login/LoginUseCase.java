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

        // Create / rotate login session
        LoginSession loginSession =
                createLoginSession(user, refreshTokenExpiresAt);

        
        return new LoginResult(
                jwtService.generateAccessToken(
                        user,
                        Instant.now(),
                        accessTokenExpiresAt
                ),
                jwtService.generateRefreshToken(
                        user,
                        loginSession.getJti(),
                        Instant.now(),
                        refreshTokenExpiresAt
                ),
                refreshTokenExpiresAt
        );
    }


    // PRIVATE METHODS
    private LoginSession createLoginSession(
            User user,
            Instant expiresAt
    ) {

        loginSessionRepository
                .findFirstByUserIdAndRevokedReasonIsNull(user.getId())
                .ifPresent(previousSession -> {
                    previousSession.setRevokedReason(
                            RevokedReason.LOGIN_ROTATION
                    );
                    previousSession.setRevokedAt(Instant.now());

                    loginSessionRepository.save(previousSession);
                });

        LoginSession loginSession = new LoginSession();

        loginSession.setUser(user);
        loginSession.setJti(jwtService.generateJti());
        loginSession.setExpiresAt(expiresAt);
        loginSession.setRevokedReason(null);
        loginSession.setRevokedAt(null);

        return loginSessionRepository.save(loginSession);
    }

}
