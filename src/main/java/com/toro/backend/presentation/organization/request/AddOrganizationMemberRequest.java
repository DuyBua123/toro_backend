package com.toro.backend.presentation.organization.request;

import com.toro.backend.infrastructure.database.enums.OrganizationDepartment;
import com.toro.backend.infrastructure.database.enums.OrganizationRole;
import com.toro.backend.infrastructure.validator.EnumExist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddOrganizationMemberRequest(
    @NotNull(message = "User id is required.")
    Long userId,

    @NotBlank(message = "Organization role is required.")
    @EnumExist(enumClass = OrganizationRole.class, message = "Organization role does not exist.")
    String organizationRole,

    @NotBlank(message = "Organization department is required.")
    @EnumExist(enumClass = OrganizationDepartment.class, message = "Organization department does not exist.")
    String organizationDepartment
) {

}
