package com.toro.backend.application.organization.add_organization_member;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.OrganizationDepartment;
import com.toro.backend.infrastructure.database.enums.OrganizationRole;
import com.toro.backend.infrastructure.database.models.OrganizationMember;
import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.OrganizationMemberRepository;
import com.toro.backend.presentation.organization.request.AddOrganizationMemberRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddOrganizationMemberUseCase {

    private final AddOrganizationMemberValidator addOrganizationMemberValidator;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Transactional
    public AddOrganizationMemberResult execute(Long organizationBranchId, AddOrganizationMemberRequest request) {
        AddOrganizationMemberValidationResult validationResult = addOrganizationMemberValidator.validate(
            organizationBranchId,
            request.userId()
        );

        OrganizationMember organizationMember = OrganizationMember.builder()
            .user(validationResult.user())
            .organizationBranch(validationResult.organizationBranch())
            .organizationRole(OrganizationRole.valueOf(request.organizationRole()))
            .organizationDepartment(OrganizationDepartment.valueOf(request.organizationDepartment()))
            .build();

        OrganizationMember savedOrganizationMember = organizationMemberRepository.save(organizationMember);

        return toResult(savedOrganizationMember);
    }

    // PRIVATE METHODS
    private AddOrganizationMemberResult toResult(OrganizationMember organizationMember) {
        User user = organizationMember.getUser();

        return new AddOrganizationMemberResult(
            user.getId(),
            organizationMember.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            organizationMember.getOrganizationRole(),
            organizationMember.getOrganizationDepartment(),
            organizationMember.getCreatedAt(),
            organizationMember.getUpdatedAt()
        );
    }

}
