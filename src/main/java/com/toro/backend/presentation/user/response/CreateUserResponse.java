package com.toro.backend.presentation.user.response;

import java.time.Instant;

public record CreateUserResponse(
    Long id,
    String fullName,
    String email,
    String phoneNumber,
    boolean isActive,
    Instant createdAt
) {

}
