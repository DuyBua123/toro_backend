package com.toro.backend.application.organization.search_organizations;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.exception.BusinessValidationException;

@Component
public class SearchOrganizationsValidator {

    private static final Set<String> SUPPORTED_FIELDS = Set.of(
        "organizationCode",
        "organizationName",
        "organizationType",
        "taxCode",
        "blockchainWallet"
    );

    public void validate(String field, String value) {
        if (!SUPPORTED_FIELDS.contains(normalizeField(field))) {
            throw new BusinessValidationException("Invalid organization search field.");
        }
    }

    public String normalizeField(String field) {
        return switch (field.trim()) {
            case "organization_code" -> "organizationCode";
            case "organization_name" -> "organizationName";
            case "organization_type" -> "organizationType";
            case "tax_code" -> "taxCode";
            case "blockchain_wallet" -> "blockchainWallet";
            default -> field.trim();
        };
    }

}
