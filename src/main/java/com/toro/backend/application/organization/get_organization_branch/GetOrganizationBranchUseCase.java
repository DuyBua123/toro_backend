package com.toro.backend.application.organization.get_organization_branch;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetOrganizationBranchUseCase {

    private final GetOrganizationBranchValidator getOrganizationBranchValidator;

    @Transactional(readOnly = true)
    public GetOrganizationBranchResult execute(Long organizationId, Long organizationBranchId) {
        OrganizationBranch organizationBranch = getOrganizationBranchValidator.validate(organizationId, organizationBranchId);

        return toResult(organizationBranch);
    }

    // PRIVATE METHODS
    private GetOrganizationBranchResult toResult(OrganizationBranch organizationBranch) {
        return new GetOrganizationBranchResult(
            organizationBranch.getId(),
            organizationBranch.getCountry(),
            organizationBranch.getAddress(),
            organizationBranch.getBranchType(),
            organizationBranch.getCreatedAt(),
            organizationBranch.getUpdatedAt()
        );
    }

}
