package com.toro.backend.application.organization.update_organization_branch;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.BranchType;
import com.toro.backend.infrastructure.database.models.OrganizationBranch;
import com.toro.backend.infrastructure.database.repository.OrganizationBranchRepository;
import com.toro.backend.presentation.organization.request.UpdateOrganizationBranchRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateOrganizationBranchUseCase {

    private final UpdateOrganizationBranchValidator updateOrganizationBranchValidator;
    private final OrganizationBranchRepository organizationBranchRepository;


    @Transactional
    public UpdateOrganizationBranchResult execute(
        Long organizationId,
        Long organizationBranchId,
        UpdateOrganizationBranchRequest request
    ) {
        OrganizationBranch organizationBranch = updateOrganizationBranchValidator.validate(
            organizationId,
            organizationBranchId
        );

        organizationBranch.setCountry(request.country());
        organizationBranch.setAddress(request.address());
        organizationBranch.setBranchType(BranchType.valueOf(request.branchType()));

        OrganizationBranch savedOrganizationBranch = organizationBranchRepository.save(organizationBranch);

        return toResult(savedOrganizationBranch);
    }


    // PRIVATE METHODS
    private UpdateOrganizationBranchResult toResult(OrganizationBranch organizationBranch) {
        return new UpdateOrganizationBranchResult(
            organizationBranch.getId(),
            organizationBranch.getCountry(),
            organizationBranch.getAddress(),
            organizationBranch.getBranchType(),
            organizationBranch.getCreatedAt(),
            organizationBranch.getUpdatedAt()
        );
    }

}
