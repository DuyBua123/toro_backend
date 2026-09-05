package com.toro.backend.presentation.authentication.request;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
    @NotNull(message = "Not allow null")
    String accessToken
) {

}
