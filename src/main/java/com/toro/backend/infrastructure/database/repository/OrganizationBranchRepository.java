package com.toro.backend.infrastructure.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;

public interface OrganizationBranchRepository extends JpaRepository<OrganizationBranch, Long> {

}
