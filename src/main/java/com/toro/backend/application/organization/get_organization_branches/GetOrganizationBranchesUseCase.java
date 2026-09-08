package com.toro.backend.application.organization.get_organization_branches;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.models.OrganizationBranch;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetOrganizationBranchesUseCase {

    private final GetOrganizationBranchesValidator getOrganizationBranchesValidator;

    @Transactional(readOnly = true)
    public List<GetOrganizationBranchesResult> execute(Long organizationId) {
        Organization organization = getOrganizationBranchesValidator.validate(organizationId);

        return organization.getBranches()
            .stream()
            .map(this::toResult)
            .toList();
    }

    // PRIVATE METHODS
    private GetOrganizationBranchesResult toResult(OrganizationBranch organizationBranch) {
        return new GetOrganizationBranchesResult(
            organizationBranch.getId(),
            organizationBranch.getCountry(),
            organizationBranch.getAddress(),
            organizationBranch.getBranchType(),
            organizationBranch.getCreatedAt(),
            organizationBranch.getUpdatedAt()
        );
    }

}
