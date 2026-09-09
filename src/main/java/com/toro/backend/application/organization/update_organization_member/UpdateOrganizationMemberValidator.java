package com.toro.backend.application.organization.update_organization_member;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.OrganizationMember;
import com.toro.backend.infrastructure.database.repository.OrganizationMemberRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateOrganizationMemberValidator {

    private final OrganizationMemberRepository organizationMemberRepository;

    public OrganizationMember validate(Long memberId) {
        return organizationMemberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessValidationException("Organization member not found in organization branch."));
    }

}
