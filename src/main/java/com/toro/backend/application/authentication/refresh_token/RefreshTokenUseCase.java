package com.toro.backend.application.authentication.refresh_token;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.RevokedReason;
import com.toro.backend.infrastructure.database.models.LoginSession;
import com.toro.backend.infrastructure.database.repository.LoginSessionRepository;
import com.toro.backend.infrastructure.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final RefreshTokenValidator refreshTokenValidator;
    private final LoginSessionRepository loginSessionRepository;
    private final JwtService jwtService;


    public Optional<RefreshTokenResult> execute(String accessToken, String refreshToken) {

        
        /*
        CASE 1: Refresh token is INVALID => Throw new Business Validation Exception (Since this refresh token is not issued || not existed || not owned have owned user in backend)
        CASE 2: Refresh token is EXPIRED => Revoke refresh token as SESSION_EXPIRED
        CASE 3: Refresh token is VALID and NOT EXPIRED:
            CASE 3.1: Access token is MISSING/EMPTY => Generate new access token ONLY with previous acess token expires at
            CASE 3.2: Access token is not MISSING/EMPTY => Generate new access token AND refresh token
        */
       
        // CASE 1
        LoginSession currentLoginSession = refreshTokenValidator.validate(refreshToken);

        // CASE 2
        if (isExpired(currentLoginSession)) {
            currentLoginSession.setRevokedAt(Instant.now());
            currentLoginSession.setRevokedReason(RevokedReason.SESSION_EXPIRED);

            loginSessionRepository.save(currentLoginSession);

            return Optional.ofNullable(null);
        }


        // CASE 3.1
        if (accessToken == null | accessToken.isBlank()) {
            System.out.println("Issue new Access Token When Page Refresh!!!!");

            Instant currentAccessTokenExpiresAt = currentLoginSession.getAccessExpiresAt();
            String newAccessToken = jwtService.generateAccessToken(
                currentLoginSession.getUser(), 
                Instant.now(), 
                currentAccessTokenExpiresAt);

            RefreshTokenResult refreshTokenResult = new RefreshTokenResult(
                newAccessToken,
                refreshToken,
                currentLoginSession.getRefreshExpiresAt()
            );

            return Optional.ofNullable(refreshTokenResult);
        }

        // CASE 3.2
        System.out.println("Issue new Access Token AND Refresh Token when Acess Token EXPIRED!!!!");

        Instant now = Instant.now();

        Instant accessTokenExpiresAt =
            jwtService.generateAccessTokenExpiresAt();

        Instant refreshTokenExpiresAt =
            jwtService.generateRefreshTokenExpiresAt();


        String newAccessToken =
            jwtService.generateAccessToken(
                currentLoginSession.getUser(),
                now,
                accessTokenExpiresAt
            );

        String newRefreshToken = jwtService.generateRefreshToken();
        String hashedRefreshToken = jwtService.hashRefreshToken(newRefreshToken);

        // Update previous login session
        currentLoginSession.setHashedRefreshToken(hashedRefreshToken);
        currentLoginSession.setAccessExpiresAt(accessTokenExpiresAt);
        currentLoginSession.setRefreshExpiresAt(refreshTokenExpiresAt);

        loginSessionRepository.save(currentLoginSession);

        RefreshTokenResult refreshTokenResult = new RefreshTokenResult(
                newAccessToken,
                newRefreshToken,
                refreshTokenExpiresAt
        );

        return Optional.ofNullable(refreshTokenResult);
    }


    // PRIVATE METHODS
    private boolean isExpired(LoginSession validLoginSession) {

        Instant refreshExpiresAt = validLoginSession.getRefreshExpiresAt();

        if (!refreshExpiresAt.isAfter(Instant.now())) {
            System.out.println("Login Session expired");
            return true;
        }

        return false;
    }

}
