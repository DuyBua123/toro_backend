package com.toro.backend.application.organization.update_organization;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.OrganizationType;

public record UpdateOrganizationResult(
    Long id,
    String organizationCode,
    String organizationName,
    OrganizationType organizationType,
    String taxCode,
    String blockchainWallet,
    Instant createdAt,
    Instant updatedAt
) {

}
