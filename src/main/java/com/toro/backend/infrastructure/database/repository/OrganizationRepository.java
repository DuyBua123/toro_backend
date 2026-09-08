package com.toro.backend.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toro.backend.infrastructure.database.models.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    boolean existsByOrganizationCode(String organizationCode);

    boolean existsByTaxCode(String taxCode);

}
