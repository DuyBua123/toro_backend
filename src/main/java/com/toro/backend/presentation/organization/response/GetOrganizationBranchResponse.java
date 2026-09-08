package com.toro.backend.presentation.organization.response;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.BranchType;

public record GetOrganizationBranchResponse(
    Long id,
    String country,
    String address,
    BranchType branchType,
    Instant createdAt,
    Instant updatedAt
) {

}
