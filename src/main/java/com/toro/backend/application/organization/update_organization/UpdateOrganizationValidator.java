package com.toro.backend.application.organization.update_organization;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.repository.OrganizationRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;
import com.toro.backend.presentation.organization.request.UpdateOrganizationRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateOrganizationValidator {

    private final OrganizationRepository organizationRepository;


    public Organization validate(Long organizationId, UpdateOrganizationRequest request) {
        Organization organization = organizationRepository.findById(organizationId)
            .orElseThrow(() -> new BusinessValidationException("Organization not found."));

        if (organizationRepository.existsByOrganizationCodeAndIdNot(request.organizationCode(), organizationId)) {
            throw new BusinessValidationException("Organization code already exists.");
        }

        if (organizationRepository.existsByTaxCodeAndIdNot(request.taxCode(), organizationId)) {
            throw new BusinessValidationException("Tax code already exists.");
        }

        return organization;
    }

}
