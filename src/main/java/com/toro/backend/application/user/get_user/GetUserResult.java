package com.toro.backend.application.user.get_user;

import java.time.Instant;

public record GetUserResult(
    Long id,
    String fullName,
    String email,
    String phoneNumber,
    boolean isActive,
    Instant createdAt
) {

}
