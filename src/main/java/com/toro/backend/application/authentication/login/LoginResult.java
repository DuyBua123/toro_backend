package com.toro.backend.application.authentication.login;

import java.time.Instant;

public record LoginResult(
    String accessToken,
    String refreshToken,
    Instant refreshTokenExpiresAt
) {

}
