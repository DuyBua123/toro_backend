package com.toro.backend.application.user.update_user;

import java.time.Instant;

public record UpdateUserResult(
    Long id,
    String fullName,
    String email,
    String phoneNumber,
    boolean isActive,
    Instant createdAt
) {

}
