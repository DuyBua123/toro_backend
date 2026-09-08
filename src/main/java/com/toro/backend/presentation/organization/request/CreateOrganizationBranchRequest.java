package com.toro.backend.presentation.organization.request;

import com.toro.backend.infrastructure.database.enums.BranchType;
import com.toro.backend.infrastructure.validator.EnumExist;

import jakarta.validation.constraints.NotBlank;

public record CreateOrganizationBranchRequest(
    @NotBlank
    String country,
    @NotBlank
    String address,
    @NotBlank
    @EnumExist(enumClass = BranchType.class, message = "Branch type does not exist.")
    String branchType
) {

}
