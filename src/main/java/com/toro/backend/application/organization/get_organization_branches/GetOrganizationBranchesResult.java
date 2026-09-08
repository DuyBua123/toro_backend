package com.toro.backend.application.organization.get_organization_branches;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.BranchType;

public record GetOrganizationBranchesResult(
    Long id,
    String country,
    String address,
    BranchType branchType,
    Instant createdAt,
    Instant updatedAt
) {

}
