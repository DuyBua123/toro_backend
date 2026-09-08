package com.toro.backend.presentation.organization.request;

import com.toro.backend.infrastructure.database.enums.OrganizationType;
import com.toro.backend.infrastructure.validator.EnumExist;

import jakarta.validation.constraints.NotBlank;

public record CreateOrganizationRequest(
    @NotBlank
    String organizationCode,
    @NotBlank
    String organizationName,
    @NotBlank
    @EnumExist(enumClass = OrganizationType.class, message = "Organization type does not exist.")
    String organizationType,
    @NotBlank
    String taxCode,
    String blockchainWallet
) {

}
