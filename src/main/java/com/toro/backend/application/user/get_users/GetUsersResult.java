package com.toro.backend.application.user.get_users;

import java.time.Instant;

public record GetUsersResult(
    Long id,
    String fullName,
    String email,
    String phoneNumber,
    boolean isActive,
    Instant createdAt
) {

}
