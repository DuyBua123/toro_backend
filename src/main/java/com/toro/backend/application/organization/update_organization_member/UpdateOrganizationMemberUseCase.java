package com.toro.backend.application.organization.update_organization_member;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.enums.OrganizationDepartment;
import com.toro.backend.infrastructure.database.enums.OrganizationRole;
import com.toro.backend.infrastructure.database.models.OrganizationMember;
import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.OrganizationMemberRepository;
import com.toro.backend.presentation.organization.request.UpdateOrganizationMemberRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateOrganizationMemberUseCase {

    private final UpdateOrganizationMemberValidator updateOrganizationMemberValidator;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Transactional
    public UpdateOrganizationMemberResult execute(Long memberId, UpdateOrganizationMemberRequest request) {
        OrganizationMember organizationMember = updateOrganizationMemberValidator.validate(memberId);

        organizationMember.setOrganizationRole(OrganizationRole.valueOf(request.organizationRole()));
        organizationMember.setOrganizationDepartment(OrganizationDepartment.valueOf(request.organizationDepartment()));

        OrganizationMember savedOrganizationMember = organizationMemberRepository.save(organizationMember);

        return toResult(savedOrganizationMember);
    }

    // PRIVATE METHODS
    private UpdateOrganizationMemberResult toResult(OrganizationMember organizationMember) {
        User user = organizationMember.getUser();

        return new UpdateOrganizationMemberResult(
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
