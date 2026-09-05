package com.toro.backend.application.authentication.bootstrap_token;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.LoginSession;
import com.toro.backend.infrastructure.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class BootstrapTokenUseCase {

    private final BootstrapTokenValidator bootstrapTokenValidator;
    private final JwtService jwtService;
    
    
    public BootstrapTokenResult execute(String refreshToken) {

        LoginSession currentLoginSession = bootstrapTokenValidator.validate(refreshToken);

        Instant currentAccessTokenExpiresAt = currentLoginSession.getAccessExpiresAt();
        String newAccessToken = jwtService.generateAccessToken(
            currentLoginSession.getUser(), 
            Instant.now(), 
            currentAccessTokenExpiresAt);

        return new BootstrapTokenResult(
            newAccessToken,
            refreshToken,
            currentLoginSession.getRefreshExpiresAt()
        );
    }

}
