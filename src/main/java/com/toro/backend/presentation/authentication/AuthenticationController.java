package com.toro.backend.presentation.authentication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toro.backend.application.authentication.login.LoginResult;
import com.toro.backend.application.authentication.login.LoginUseCase;
import com.toro.backend.application.authentication.refresh_token.RefreshTokenResult;
import com.toro.backend.application.authentication.refresh_token.RefreshTokenUseCase;
import com.toro.backend.infrastructure.api.SuccessResponse;
import com.toro.backend.infrastructure.exception.InvalidRefreshTokenException;
import com.toro.backend.infrastructure.exception.UnauthenticatedException;
import com.toro.backend.presentation.authentication.request.LoginRequest;
import com.toro.backend.presentation.authentication.request.RefreshTokenRequest;
import com.toro.backend.presentation.authentication.response.LoginResponse;
import com.toro.backend.presentation.authentication.response.RefreshTokenResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;



    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(
        @Valid @RequestBody LoginRequest request
    ) {

        LoginResult result = loginUseCase.execute(request);

        ResponseCookie refreshTokenCookie = ResponseCookie
            .from("refreshToken", result.refreshToken())
            .httpOnly(true)
            .secure(false) 
            .sameSite(SameSite.LAX.toString())
            .path("/api/auth/refresh-token")
            .maxAge(Duration.between(Instant.now(), result.refreshTokenExpiresAt()))
            .build();
        
        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(
                SuccessResponse.success(
                    "Login successfully",
                    new LoginResponse(result.accessToken())
                )
            );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<RefreshTokenResponse>> refreshToken(
        @RequestBody RefreshTokenRequest request,
        @CookieValue(
            name = "refreshToken",
            required = false
        ) String refreshToken
    ) {

        Optional<RefreshTokenResult> result = refreshTokenUseCase
            .execute(request.accessToken(), refreshToken);

        if (!result.isPresent()) {
            // refreshToken cookie is automatically removed when EXPIRED

            throw new InvalidRefreshTokenException("Login Session is Expired");
        }

        ResponseCookie refreshTokenCookie = ResponseCookie
            .from("refreshToken", result.get().refreshToken())
            .httpOnly(true)
            .secure(false) 
            .sameSite(SameSite.LAX.toString())
            .path("/api/auth/refresh-token")
            .maxAge(Duration.between(Instant.now(), result.get().refreshTokenExpiresAt()))
            .build();
        
        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(
                SuccessResponse.success(
                    "Refresh token successfully",
                    new RefreshTokenResponse(result.get().accessToken())
                )
            );
    }

}
