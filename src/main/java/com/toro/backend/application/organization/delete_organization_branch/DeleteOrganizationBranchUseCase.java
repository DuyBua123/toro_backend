package com.toro.backend.application.organization.delete_organization_branch;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;
import com.toro.backend.infrastructure.database.repository.OrganizationBranchRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteOrganizationBranchUseCase {

    private final DeleteOrganizationBranchValidator deleteOrganizationBranchValidator;
    private final OrganizationBranchRepository organizationBranchRepository;


    @Transactional
    public void execute(Long organizationBranchId) {
        OrganizationBranch organizationBranch = deleteOrganizationBranchValidator.validate(
            organizationBranchId
        );

        organizationBranchRepository.delete(organizationBranch);
    }

}
