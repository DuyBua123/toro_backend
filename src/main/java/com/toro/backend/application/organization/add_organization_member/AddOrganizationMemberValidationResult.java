package com.toro.backend.application.organization.add_organization_member;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;
import com.toro.backend.infrastructure.database.models.User;

public record AddOrganizationMemberValidationResult(
    OrganizationBranch organizationBranch,
    User user
) {

}
