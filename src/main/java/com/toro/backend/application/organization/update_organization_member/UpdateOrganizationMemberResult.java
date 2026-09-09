package com.toro.backend.application.organization.update_organization_member;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.OrganizationDepartment;
import com.toro.backend.infrastructure.database.enums.OrganizationRole;

public record UpdateOrganizationMemberResult(
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
