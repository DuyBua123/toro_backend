package com.toro.backend.application.organization.search_organizations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.OrganizationType;
import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.repository.OrganizationRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchOrganizationsUseCase {

    private final OrganizationRepository organizationRepository;
    private final SearchOrganizationsValidator validator;

    public List<SearchOrganizationsResult> execute(String field, String value) {
        if (isBlank(field) || isBlank(value)) {
            return organizationRepository.findAll()
                .stream()
                .map(this::toResult)
                .toList();
        }

        validator.validate(field, value);

        List<Organization> organizations = searchByField(
            validator.normalizeField(field),
            value.trim()
        );

        return organizations
            .stream()
            .map(this::toResult)
            .toList();
    }


    // PRIVATE METHODS
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private List<Organization> searchByField(String field, String value) {
        return switch (field) {
            case "organizationCode" -> organizationRepository.findByOrganizationCodeContainingIgnoreCase(value);
            case "organizationName" -> organizationRepository.findByOrganizationNameContainingIgnoreCase(value);
            case "organizationType" -> searchByOrganizationType(value);
            case "taxCode" -> organizationRepository.findByTaxCodeContainingIgnoreCase(value);
            case "blockchainWallet" -> organizationRepository.findByBlockchainWalletContainingIgnoreCase(value);
            default -> throw new BusinessValidationException("Invalid organization search field.");
        };
    }

    private List<Organization> searchByOrganizationType(String value) {
        try {
            OrganizationType organizationType = OrganizationType.valueOf(value.trim().toUpperCase());
            return organizationRepository.findByOrganizationType(organizationType);
        } catch (IllegalArgumentException ex) {
            throw new BusinessValidationException("Invalid organization type.");
        }
    }

    private SearchOrganizationsResult toResult(Organization organization) {
        return new SearchOrganizationsResult(
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
