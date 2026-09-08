package com.toro.backend.application.organization.get_organizations;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.OrganizationType;

public record GetOrganizationsResult(
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
