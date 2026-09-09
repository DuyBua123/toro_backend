package com.toro.backend.presentation.organization.response;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.OrganizationDepartment;
import com.toro.backend.infrastructure.database.enums.OrganizationRole;

public record UpdateOrganizationMemberResponse(
    Long userId,
    Long memberId,
    String fullName,
    String email,
    String phoneNumber,
    OrganizationRole organizationRole,
    OrganizationDepartment organizationDepartment,
    Instant createdAt,
    Instant updatedAt
) {

}
