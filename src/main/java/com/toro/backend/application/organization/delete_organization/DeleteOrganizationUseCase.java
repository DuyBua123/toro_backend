package com.toro.backend.application.organization.delete_organization;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.Organization;
import com.toro.backend.infrastructure.database.repository.OrganizationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteOrganizationUseCase {

    private final DeleteOrganizationValidator deleteOrganizationValidator;
    private final OrganizationRepository organizationRepository;


    @Transactional
    public void execute(Long organizationId) {
        Organization organization = deleteOrganizationValidator.validate(organizationId);

        organizationRepository.delete(organization);
    }

}
