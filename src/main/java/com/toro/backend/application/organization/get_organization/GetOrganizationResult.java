package com.toro.backend.application.organization.get_organization;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.OrganizationType;

public record GetOrganizationResult(
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
