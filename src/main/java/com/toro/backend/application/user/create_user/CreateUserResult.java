package com.toro.backend.application.user.create_user;

import java.time.Instant;

public record CreateUserResult(
    Long id,
    String fullName,
    String email,
    String phoneNumber,
    boolean isActive,
    Instant createdAt
) {

}
