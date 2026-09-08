package com.toro.backend.application.organization.get_organization;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.Organization;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetOrganizationUseCase {

    private final GetOrganizationValidator getOrganizationValidator;

    public GetOrganizationResult execute(Long organizationId) {
        Organization organization = getOrganizationValidator.validate(organizationId);

        return toResult(organization);
    }

    // PRIVATE METHODS
    private GetOrganizationResult toResult(Organization organization) {
        return new GetOrganizationResult(
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
