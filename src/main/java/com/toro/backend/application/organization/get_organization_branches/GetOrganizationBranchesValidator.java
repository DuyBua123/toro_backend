package com.toro.backend.application.organization.get_organization_branches;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.repository.OrganizationRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetOrganizationBranchesValidator {

    private final OrganizationRepository organizationRepository;

    public Organization validate(Long organizationId) {
        return organizationRepository.findById(organizationId)
            .orElseThrow(() -> new BusinessValidationException("Organization not found."));
    }

}
