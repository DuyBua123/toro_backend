package com.toro.backend.application.organization.create_organization;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.repository.OrganizationRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;
import com.toro.backend.presentation.organization.request.CreateOrganizationRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateOrganizationValidator {

    private final OrganizationRepository organizationRepository;


    public void validate(CreateOrganizationRequest request) {
        if (organizationRepository.existsByOrganizationCode(request.organizationCode())) {
            throw new BusinessValidationException("Organization code already exists.");
        }

        if (organizationRepository.existsByTaxCode(request.taxCode())) {
            throw new BusinessValidationException("Tax code already exists.");
        }
    }

}
