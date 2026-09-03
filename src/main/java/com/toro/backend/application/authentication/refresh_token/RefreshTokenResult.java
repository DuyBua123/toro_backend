package com.toro.backend.application.authentication.refresh_token;

import java.time.Instant;

public record RefreshTokenResult(
    String accessToken,
    String refreshToken,
    Instant refreshTokenExpiresAt
) {

}
