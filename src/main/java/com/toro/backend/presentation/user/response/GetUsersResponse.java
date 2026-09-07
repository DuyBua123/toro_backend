package com.toro.backend.presentation.user.response;

import java.time.Instant;


public record GetUsersResponse(
    Long id,
    String fullName,
    String email,
    String phoneNumber,
    boolean isActive,
    Instant createdAt
) {

}
