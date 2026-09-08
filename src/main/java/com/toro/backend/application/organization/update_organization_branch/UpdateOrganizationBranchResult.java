package com.toro.backend.application.organization.update_organization_branch;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.BranchType;

public record UpdateOrganizationBranchResult(
    Long id,
    String country,
    String address,
    BranchType branchType,
    Instant createdAt,
    Instant updatedAt
) {

}
