package com.toro.backend.application.authentication.bootstrap_token;

import java.time.Instant;

public record BootstrapTokenResult(
    String accessToken,
    String refreshToken,
    Instant refreshTokenExpiresAt
) {

}
