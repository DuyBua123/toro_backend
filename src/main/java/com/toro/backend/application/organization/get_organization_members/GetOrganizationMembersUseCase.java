package com.toro.backend.application.organization.get_organization_members;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;
import com.toro.backend.infrastructure.database.models.OrganizationMember;
import com.toro.backend.infrastructure.database.models.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetOrganizationMembersUseCase {

    private final GetOrganizationMembersValidator getOrganizationMembersValidator;

    @Transactional(readOnly = true)
    public List<GetOrganizationMembersResult> execute(Long organizationBranchId) {
        OrganizationBranch organizationBranch = getOrganizationMembersValidator.validate(organizationBranchId);

        return organizationBranch.getOrganizationMembers()
            .stream()
            .map(this::toResult)
            .toList();
    }

    // PRIVATE METHODS
    private GetOrganizationMembersResult toResult(OrganizationMember organizationMember) {
        User user = organizationMember.getUser();

        return new GetOrganizationMembersResult(
            user.getId(),
            organizationMember.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            organizationMember.getOrganizationRole(),
            organizationMember.getOrganizationDepartment(),
            organizationMember.getCreatedAt(),
            organizationMember.getUpdatedAt()
        );
    }

}
