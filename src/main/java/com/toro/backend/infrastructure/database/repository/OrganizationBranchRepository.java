package com.toro.backend.infrastructure.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;

public interface OrganizationBranchRepository extends JpaRepository<OrganizationBranch, Long> {

    Optional<OrganizationBranch> findByIdAndOrganizationId(Long id, Long organizationId);

}
