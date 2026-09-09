package com.toro.backend.application.organization.add_organization_member;

import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.OrganizationBranch;
import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.OrganizationBranchRepository;
import com.toro.backend.infrastructure.database.repository.OrganizationMemberRepository;
import com.toro.backend.infrastructure.database.repository.UserRepository;
import com.toro.backend.infrastructure.exception.BusinessValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddOrganizationMemberValidator {

    private final OrganizationBranchRepository organizationBranchRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final UserRepository userRepository;

    public AddOrganizationMemberValidationResult validate(Long organizationBranchId, Long userId) {
        OrganizationBranch organizationBranch = organizationBranchRepository.findById(organizationBranchId)
            .orElseThrow(() -> new BusinessValidationException("Organization branch not found."));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessValidationException("User not found."));

        if (organizationMemberRepository.existsByUserIdAndOrganizationBranchId(userId, organizationBranchId)) {
            throw new BusinessValidationException("User already exists in organization branch.");
        }

        return new AddOrganizationMemberValidationResult(organizationBranch, user);
    }

}
