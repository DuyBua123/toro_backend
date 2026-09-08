package com.toro.backend.presentation.organization;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toro.backend.application.organization.create_organization.CreateOrganizationResult;
import com.toro.backend.application.organization.create_organization.CreateOrganizationUseCase;
import com.toro.backend.application.organization.delete_organization.DeleteOrganizationUseCase;
import com.toro.backend.application.organization.get_organization.GetOrganizationResult;
import com.toro.backend.application.organization.get_organization.GetOrganizationUseCase;
import com.toro.backend.application.organization.get_organization_branches.GetOrganizationBranchesResult;
import com.toro.backend.application.organization.get_organization_branches.GetOrganizationBranchesUseCase;
import com.toro.backend.application.organization.get_organizations.GetOrganizationsResult;
import com.toro.backend.application.organization.get_organizations.GetOrganizationsUseCase;
import com.toro.backend.application.organization.search_organizations.SearchOrganizationsResult;
import com.toro.backend.application.organization.search_organizations.SearchOrganizationsUseCase;
import com.toro.backend.application.organization.update_organization.UpdateOrganizationResult;
import com.toro.backend.application.organization.update_organization.UpdateOrganizationUseCase;
import com.toro.backend.infrastructure.api.SuccessResponse;
import com.toro.backend.presentation.organization.request.CreateOrganizationRequest;
import com.toro.backend.presentation.organization.request.UpdateOrganizationRequest;
import com.toro.backend.presentation.organization.response.CreateOrganizationResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationBranchesResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationsResponse;
import com.toro.backend.presentation.organization.response.SearchOrganizationsResponse;
import com.toro.backend.presentation.organization.response.UpdateOrganizationResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/organizations")
@RequiredArgsConstructor 
public class OrganizationController {

    private final GetOrganizationsUseCase getOrganizationsUseCase;
    private final GetOrganizationUseCase getOrganizationUseCase;
    private final SearchOrganizationsUseCase searchOrganizationsUseCase;
    private final CreateOrganizationUseCase createOrganizationUseCase;
    private final UpdateOrganizationUseCase updateOrganizationUseCase;
    private final DeleteOrganizationUseCase deleteOrganizationUseCase;

    private final GetOrganizationBranchesUseCase getOrganizationBranchesUseCase;


    // ================================== Organization ===============================================
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

    @GetMapping("/search-organizations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<SearchOrganizationsResponse>>> searchOrganizations(
        @RequestParam(value = "field", required = false) String field,
        @RequestParam(value = "value", required = false) String value
    ) {
        List<SearchOrganizationsResponse> response = searchOrganizationsUseCase.execute(field, value)
            .stream()
            .map(this::toSearchOrganizationsResponse)
            .toList();

        return ResponseEntity.ok(
            SuccessResponse.success("Search organizations successfully", response)
        );
    }

    @GetMapping("/get-organization")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<GetOrganizationResponse>> getOrganization(
        @RequestParam ("organization_id")
        @NotNull(message = "Organization id is required") Long organizationId
    ) {
        GetOrganizationResponse response = toGetOrganizationResponse(getOrganizationUseCase.execute(organizationId));

        return ResponseEntity.ok(
            SuccessResponse.success("Get organization successfully", response)
        );
    }

    @PostMapping("/create-organization")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<CreateOrganizationResponse>> createOrganization(
        @Valid @RequestBody CreateOrganizationRequest request
    ) {
        CreateOrganizationResponse response = toCreateOrganizationResponse(createOrganizationUseCase.execute(request));

        return ResponseEntity.ok(
            SuccessResponse.success("Create organization successfully", response)
        );
    }

    @PutMapping("/update-organization")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<UpdateOrganizationResponse>> updateOrganization(
        @RequestParam ("organization_id")
        @NotNull(message = "Organization id is required") Long organizationId,
        @Valid @RequestBody UpdateOrganizationRequest request
    ) {
        UpdateOrganizationResponse response = toUpdateOrganizationResponse(updateOrganizationUseCase.execute(organizationId, request));

        return ResponseEntity.ok(
            SuccessResponse.success("Update organization successfully", response)
        );
    }

    @DeleteMapping("/delete-organization/{organization_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Void>> deleteOrganization(
        @RequestParam ("organization_id")
        @NotNull(message = "Organization id is required") Long organizationId
    ) {
        deleteOrganizationUseCase.execute(organizationId);

        return ResponseEntity.ok(
            SuccessResponse.successMessage("Delete organization successfully")
        );
    }


    // ================================== Organization Branch =============================================
    @GetMapping("/get-organization-branches")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<GetOrganizationBranchesResponse>>> getOrganizationBranches(
        @RequestParam("organization_id")
        @NotNull(message = "Organization id is required") Long organizationId
    ) {
        List<GetOrganizationBranchesResponse> response = getOrganizationBranchesUseCase.execute(organizationId)
            .stream()
            .map(this::toGetOrganizationBranchesResponse)
            .toList();

        return ResponseEntity.ok(
            SuccessResponse.success("Get organization branches successfully", response)
        );
    }
    



    // PRIVATE METHODS
    private CreateOrganizationResponse toCreateOrganizationResponse(CreateOrganizationResult result) {
        return new CreateOrganizationResponse(
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

    private UpdateOrganizationResponse toUpdateOrganizationResponse(UpdateOrganizationResult result) {
        return new UpdateOrganizationResponse(
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

    private SearchOrganizationsResponse toSearchOrganizationsResponse(SearchOrganizationsResult result) {
        return new SearchOrganizationsResponse(
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


    private GetOrganizationBranchesResponse toGetOrganizationBranchesResponse(GetOrganizationBranchesResult result) {
        return new GetOrganizationBranchesResponse(
            result.id(),
            result.country(),
            result.address(),
            result.branchType(),
            result.createdAt(),
            result.updatedAt()
        );
    }

}
