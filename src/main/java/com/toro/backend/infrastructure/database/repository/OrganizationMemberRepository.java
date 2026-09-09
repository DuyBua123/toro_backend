package com.toro.backend.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toro.backend.infrastructure.database.models.OrganizationMember;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {

    boolean existsByUserIdAndOrganizationBranchId(Long userId, Long organizationBranchId);

}
