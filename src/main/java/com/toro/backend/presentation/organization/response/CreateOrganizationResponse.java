package com.toro.backend.presentation.organization.response;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.OrganizationType;

public record CreateOrganizationResponse(
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
