package com.toro.backend.application.organization.get_organizations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class GetOrganizationsUseCase {

    private final OrganizationRepository organizationRepository;

    public List<GetOrganizationsResult> execute() {
        return organizationRepository.findAll()
            .stream()
            .map(this::toResult)
            .toList();
    }


    // PRIVATE METHODS
    private GetOrganizationsResult toResult(Organization organization) {
        return new GetOrganizationsResult(
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
