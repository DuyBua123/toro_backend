package com.toro.backend.application.organization.create_organization_branch;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.BranchType;
import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.models.OrganizationBranch;
import com.toro.backend.infrastructure.database.repository.OrganizationBranchRepository;
import com.toro.backend.presentation.organization.request.CreateOrganizationBranchRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateOrganizationBranchUseCase {

    private final CreateOrganizationBranchValidator createOrganizationBranchValidator;
    private final OrganizationBranchRepository organizationBranchRepository;


    @Transactional
    public CreateOrganizationBranchResult execute(Long organizationId, CreateOrganizationBranchRequest request) {
        Organization organization = createOrganizationBranchValidator.validate(organizationId);

        OrganizationBranch organizationBranch = OrganizationBranch.builder()
            .country(request.country())
            .address(request.address())
            .branchType(BranchType.valueOf(request.branchType()))
            .organization(organization)
            .build();

        OrganizationBranch savedOrganizationBranch = organizationBranchRepository.save(organizationBranch);

        return toResult(savedOrganizationBranch);
    }


    // PRIVATE METHODS
    private CreateOrganizationBranchResult toResult(OrganizationBranch organizationBranch) {
        return new CreateOrganizationBranchResult(
            organizationBranch.getId(),
            organizationBranch.getCountry(),
            organizationBranch.getAddress(),
            organizationBranch.getBranchType(),
            organizationBranch.getCreatedAt(),
            organizationBranch.getUpdatedAt()
        );
    }

}
