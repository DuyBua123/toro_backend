package com.toro.backend.presentation.organization;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toro.backend.application.organization.get_organization.GetOrganizationResult;
import com.toro.backend.application.organization.get_organization.GetOrganizationUseCase;
import com.toro.backend.application.organization.get_organizations.GetOrganizationsResult;
import com.toro.backend.application.organization.get_organizations.GetOrganizationsUseCase;
import com.toro.backend.infrastructure.api.SuccessResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationsResponse;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/organizations")
@RequiredArgsConstructor 
public class OrganizationController {

    private final GetOrganizationsUseCase getOrganizationsUseCase;
    private final GetOrganizationUseCase getOrganizationUseCase;


    @GetMapping("/get-organizations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<GetOrganizationsResponse>>> getOrganizations() {
        List<GetOrganizationsResponse> response = getOrganizationsUseCase.execute()
            .stream()
            .map(this::toGetOrganizationsResponse)
            .toList();

        return ResponseEntity.ok(
            SuccessResponse.success("Get organizations successfully", response)
        );
    }

    @GetMapping("/get-organization/{organization_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<GetOrganizationResponse>> getOrganization(
        @PathVariable("organization_id")
        @NotNull(message = "Organization id is required") Long organizationId
    ) {
        GetOrganizationResponse response = toGetOrganizationResponse(getOrganizationUseCase.execute(organizationId));

        return ResponseEntity.ok(
            SuccessResponse.success("Get organization successfully", response)
        );
    }


    // PRIVATE METHODS
    private GetOrganizationResponse toGetOrganizationResponse(GetOrganizationResult result) {
        return new GetOrganizationResponse(
            result.id(),
            result.organizationCode(),
            result.organizationName(),
            result.organizationType(),
            result.taxCode(),
            result.blockchainWallet(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    private GetOrganizationsResponse toGetOrganizationsResponse(GetOrganizationsResult result) {
        return new GetOrganizationsResponse(
            result.id(),
            result.organizationCode(),
            result.organizationName(),
            result.organizationType(),
            result.taxCode(),
            result.blockchainWallet(),
            result.createdAt(),
            result.updatedAt()
        );
    }

}
