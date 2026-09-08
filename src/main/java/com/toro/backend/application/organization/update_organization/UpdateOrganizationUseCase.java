package com.toro.backend.application.organization.update_organization;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.OrganizationType;
import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.repository.OrganizationRepository;
import com.toro.backend.presentation.organization.request.UpdateOrganizationRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateOrganizationUseCase {

    private final UpdateOrganizationValidator updateOrganizationValidator;
    private final OrganizationRepository organizationRepository;


    @Transactional
    public UpdateOrganizationResult execute(Long organizationId, UpdateOrganizationRequest request) {
        Organization organization = updateOrganizationValidator.validate(organizationId, request);

        organization.setOrganizationCode(request.organizationCode());
        organization.setOrganizationName(request.organizationName());
        organization.setOrganizationType(OrganizationType.valueOf(request.organizationType()));
        organization.setTaxCode(request.taxCode());
        organization.setBlockchainWallet(request.blockchainWallet());

        Organization savedOrganization = organizationRepository.save(organization);

        return toResult(savedOrganization);
    }


    // PRIVATE METHODS
    private UpdateOrganizationResult toResult(Organization organization) {
        return new UpdateOrganizationResult(
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
