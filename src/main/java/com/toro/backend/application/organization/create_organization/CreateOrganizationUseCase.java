package com.toro.backend.application.organization.create_organization;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.OrganizationType;
import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.repository.OrganizationRepository;
import com.toro.backend.presentation.organization.request.CreateOrganizationRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateOrganizationUseCase {

    private final CreateOrganizationValidator createOrganizationValidator;
    private final OrganizationRepository organizationRepository;


    @Transactional
    public CreateOrganizationResult execute(CreateOrganizationRequest request) {
        createOrganizationValidator.validate(request);

        Organization organization = Organization.builder()
            .organizationCode(request.organizationCode())
            .organizationName(request.organizationName())
            .organizationType(OrganizationType.valueOf(request.organizationType()))
            .taxCode(request.taxCode())
            .blockchainWallet(request.blockchainWallet())
            .build();

        Organization savedOrganization = organizationRepository.save(organization);

        return toResult(savedOrganization);
    }


    // PRIVATE METHODS
    private CreateOrganizationResult toResult(Organization organization) {
        return new CreateOrganizationResult(
            organization.getId(),
            organization.getOrganizationCode(),
            organization.getOrganizationName(),
            organization.getOrganizationType(),
            organization.getTaxCode(),
            organization.getBlockchainWallet(),
            organization.getCreatedAt(),
            organization.getUpdatedAt()
        );
    }

}
