package com.toro.backend.application.authentication.refresh_token;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.LoginSession;
import com.toro.backend.infrastructure.database.repository.LoginSessionRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;
import com.toro.backend.infrastructure.security.jwt.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenValidator {

    private final JwtService jwtService;
    private final LoginSessionRepository loginSessionRepository;


    public LoginSession validate(String refreshToken) {

        // 1. Ensure refresh token exists
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessValidationException(
                    "Refresh token not found"
            );
        }

        Claims claims;

        try {

            // 2. Validate JWT:
            // - Signature
            // - Issuer
            // - Expiration
            claims = jwtService.parseClaims(refreshToken);

        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("JWT Exception: " + e.getMessage());
            throw new BusinessValidationException("Refresh token invalid");
        }

        // 3. Extract required claims
        String currentSub = claims.getSubject();
        String currentJti = claims.getId();

        Date expiration = claims.getExpiration();

        Instant currentExpiration =
                expiration != null
                        ? expiration.toInstant()
                        : null;

        // 4. Validate required claims
        if (currentSub == null ||
            currentSub.isBlank() ||
            currentJti == null ||
            currentJti.isBlank() ||
            currentExpiration == null) {

            System.out.println("Invalid refresh token: Missing required claims");
            throw new BusinessValidationException("Refresh token invalid");
        }

        // 5. Parse subject
        Long userId;

        try {
            userId = Long.parseLong(currentSub);
        } catch (NumberFormatException e) {
            System.out.println("Invalid refresh token: Subject is not a valid user ID");
            throw new BusinessValidationException("Refresh token invalid");
        }

        // 6. Load login session
        LoginSession currentLoginSession = loginSessionRepository
            .findFirstByJtiAndUserId(currentJti,userId)
            .orElseThrow(() -> {
                System.out.println("Invalid refresh token: Session not found");
                return new BusinessValidationException("Refresh token invalid");
            });

        // 7. Verify session user exists
        if (currentLoginSession.getUser() == null) {
            System.out.println("Invalid refresh token: User not found");
            throw new BusinessValidationException("Refresh token invalid");
        }

        // 8. Verify session is not revoked
        if (currentLoginSession.getRevokedAt() != null) {
            System.out.println("Invalid refresh token: Session revoked");
            throw new BusinessValidationException("Refresh token invalid");
        }

        // 9. Verify session hasn't expired
        if (!currentLoginSession
                .getExpiresAt()
                .isAfter(Instant.now())) {

            System.out.println("Invalid refresh token: Session expired");
            throw new BusinessValidationException("Refresh token invalid");
        }

        // 10. Verify JWT expiration matches database
        if (!currentExpiration.equals(
                currentLoginSession.getExpiresAt()
        )) {

            System.out.println("Invalid refresh token: Expiration mismatch");
            System.out.println("JWT Expiration: " + currentExpiration);
            System.out.println("Database Expiration: " + currentLoginSession.getExpiresAt());
            throw new BusinessValidationException("Refresh token invalid");
        }

        return currentLoginSession;
    }

}
