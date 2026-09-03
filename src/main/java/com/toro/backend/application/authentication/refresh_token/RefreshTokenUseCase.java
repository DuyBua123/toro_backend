package com.toro.backend.application.authentication.refresh_token;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.RevokedReason;
import com.toro.backend.infrastructure.database.models.LoginSession;
import com.toro.backend.infrastructure.database.repository.LoginSessionRepository;
import com.toro.backend.infrastructure.security.jwt.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final RefreshTokenValidator refreshTokenValidator;
    private final LoginSessionRepository loginSessionRepository;
    private final JwtService jwtService;


    @Transactional
    public RefreshTokenResult execute(String refreshToken) {

        LoginSession currentLoginSession =
            refreshTokenValidator.validate(refreshToken);

        Instant now = Instant.now();

        Instant accessTokenExpiresAt =
            jwtService.generateAccessTokenExpiresAt();

        Instant refreshTokenExpiresAt =
            jwtService.generateRefreshTokenExpiresAt();

        String jti = jwtService.generateJti();

        String newAccessToken =
            jwtService.generateAccessToken(
                currentLoginSession.getUser(),
                now,
                accessTokenExpiresAt
            );

        String newRefreshToken =
            jwtService.generateRefreshToken(
                currentLoginSession.getUser(),
                jti,
                now,
                refreshTokenExpiresAt
            );

        // Revoke previous login session
        currentLoginSession.setRevokedAt(now);
        currentLoginSession.setRevokedReason(RevokedReason.REFRESH_ROTATION);

        // Create new login session
        LoginSession newLoginSession = new LoginSession();

        newLoginSession.setUser(currentLoginSession.getUser());

        newLoginSession.setJti(jti);

        newLoginSession.setExpiresAt(refreshTokenExpiresAt);

        loginSessionRepository.save(newLoginSession);

        return new RefreshTokenResult(
                newAccessToken,
                newRefreshToken,
                refreshTokenExpiresAt
        );
    }

}
