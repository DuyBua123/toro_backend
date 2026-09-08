package com.toro.backend.infrastructure.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toro.backend.infrastructure.database.enums.OrganizationType;
import com.toro.backend.infrastructure.database.models.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    boolean existsByOrganizationCode(String organizationCode);

    boolean existsByTaxCode(String taxCode);

    boolean existsByOrganizationCodeAndIdNot(String organizationCode, Long id);

    boolean existsByTaxCodeAndIdNot(String taxCode, Long id);


    // Search
    List<Organization> findByOrganizationCodeContainingIgnoreCase(String organizationCode);

    List<Organization> findByOrganizationNameContainingIgnoreCase(String organizationName);

    List<Organization> findByOrganizationType(OrganizationType organizationType);

    List<Organization> findByTaxCodeContainingIgnoreCase(String taxCode);

    List<Organization> findByBlockchainWalletContainingIgnoreCase(String blockchainWallet);

}
