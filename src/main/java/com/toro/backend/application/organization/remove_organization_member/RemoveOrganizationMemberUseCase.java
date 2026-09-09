package com.toro.backend.application.organization.remove_organization_member;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.OrganizationMember;
import com.toro.backend.infrastructure.database.repository.OrganizationMemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemoveOrganizationMemberUseCase {

    private final RemoveOrganizationMemberValidator removeOrganizationMemberValidator;
    private final OrganizationMemberRepository organizationMemberRepository;


    @Transactional
    public void execute(Long memberId) {
        OrganizationMember organizationMember = removeOrganizationMemberValidator.validate(memberId);

        organizationMemberRepository.delete(organizationMember);
    }

}
