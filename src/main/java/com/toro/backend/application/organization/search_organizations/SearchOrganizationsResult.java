package com.toro.backend.application.organization.search_organizations;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.OrganizationType;

public record SearchOrganizationsResult(
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
