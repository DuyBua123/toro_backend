package com.toro.backend.application.organization.delete_organization_branch;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;
import com.toro.backend.infrastructure.database.repository.OrganizationBranchRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteOrganizationBranchValidator {

    private final OrganizationBranchRepository organizationBranchRepository;


    public OrganizationBranch validate(Long organizationBranchId) {
        return organizationBranchRepository.findById(organizationBranchId)
            .orElseThrow(() -> new BusinessValidationException("Organization branch not found."));
    }

}
