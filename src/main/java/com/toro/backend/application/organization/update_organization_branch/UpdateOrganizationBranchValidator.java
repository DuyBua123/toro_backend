package com.toro.backend.application.organization.update_organization_branch;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;
import com.toro.backend.infrastructure.database.repository.OrganizationBranchRepository;
import com.toro.backend.infrastructure.database.repository.OrganizationRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateOrganizationBranchValidator {

    private final OrganizationRepository organizationRepository;
    private final OrganizationBranchRepository organizationBranchRepository;


    public OrganizationBranch validate(Long organizationId, Long organizationBranchId) {
        if (!organizationRepository.existsById(organizationId)) {
            throw new BusinessValidationException("Organization not found.");
        }

        return organizationBranchRepository.findByIdAndOrganizationId(organizationBranchId, organizationId)
            .orElseThrow(() -> new BusinessValidationException("Organization branch not found."));
    }

}
