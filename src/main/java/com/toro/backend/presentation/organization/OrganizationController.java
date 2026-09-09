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
import com.toro.backend.application.organization.create_organization_branch.CreateOrganizationBranchResult;
import com.toro.backend.application.organization.create_organization_branch.CreateOrganizationBranchUseCase;
import com.toro.backend.application.organization.add_organization_member.AddOrganizationMemberResult;
import com.toro.backend.application.organization.add_organization_member.AddOrganizationMemberUseCase;
import com.toro.backend.application.organization.delete_organization.DeleteOrganizationUseCase;
import com.toro.backend.application.organization.delete_organization_branch.DeleteOrganizationBranchUseCase;
import com.toro.backend.application.organization.get_organization.GetOrganizationResult;
import com.toro.backend.application.organization.get_organization.GetOrganizationUseCase;
import com.toro.backend.application.organization.get_organization_branch.GetOrganizationBranchResult;
import com.toro.backend.application.organization.get_organization_branch.GetOrganizationBranchUseCase;
import com.toro.backend.application.organization.get_organization_branches.GetOrganizationBranchesResult;
import com.toro.backend.application.organization.get_organization_branches.GetOrganizationBranchesUseCase;
import com.toro.backend.application.organization.get_organization_members.GetOrganizationMembersResult;
import com.toro.backend.application.organization.get_organization_members.GetOrganizationMembersUseCase;
import com.toro.backend.application.organization.get_organizations.GetOrganizationsResult;
import com.toro.backend.application.organization.get_organizations.GetOrganizationsUseCase;
import com.toro.backend.application.organization.remove_organization_member.RemoveOrganizationMemberUseCase;
import com.toro.backend.application.organization.search_organizations.SearchOrganizationsResult;
import com.toro.backend.application.organization.search_organizations.SearchOrganizationsUseCase;
import com.toro.backend.application.organization.update_organization.UpdateOrganizationResult;
import com.toro.backend.application.organization.update_organization.UpdateOrganizationUseCase;
import com.toro.backend.application.organization.update_organization_branch.UpdateOrganizationBranchResult;
import com.toro.backend.application.organization.update_organization_branch.UpdateOrganizationBranchUseCase;
import com.toro.backend.application.organization.update_organization_member.UpdateOrganizationMemberResult;
import com.toro.backend.application.organization.update_organization_member.UpdateOrganizationMemberUseCase;
import com.toro.backend.infrastructure.api.SuccessResponse;
import com.toro.backend.presentation.organization.request.AddOrganizationMemberRequest;
import com.toro.backend.presentation.organization.request.CreateOrganizationBranchRequest;
import com.toro.backend.presentation.organization.request.CreateOrganizationRequest;
import com.toro.backend.presentation.organization.request.UpdateOrganizationBranchRequest;
import com.toro.backend.presentation.organization.request.UpdateOrganizationMemberRequest;
import com.toro.backend.presentation.organization.request.UpdateOrganizationRequest;
import com.toro.backend.presentation.organization.response.AddOrganizationMemberResponse;
import com.toro.backend.presentation.organization.response.CreateOrganizationBranchResponse;
import com.toro.backend.presentation.organization.response.CreateOrganizationResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationBranchResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationBranchesResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationMembersResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationResponse;
import com.toro.backend.presentation.organization.response.GetOrganizationsResponse;
import com.toro.backend.presentation.organization.response.SearchOrganizationsResponse;
import com.toro.backend.presentation.organization.response.UpdateOrganizationBranchResponse;
import com.toro.backend.presentation.organization.response.UpdateOrganizationMemberResponse;
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
    private final GetOrganizationBranchUseCase getOrganizationBranchUseCase;
    private final CreateOrganizationBranchUseCase createOrganizationBranchUseCase;
    private final UpdateOrganizationBranchUseCase updateOrganizationBranchUseCase;
    private final DeleteOrganizationBranchUseCase deleteOrganizationBranchUseCase;

    private final GetOrganizationMembersUseCase getOrganizationMembersUseCase;
    private final AddOrganizationMemberUseCase addOrganizationMemberUseCase;
    private final UpdateOrganizationMemberUseCase updateOrganizationMemberUseCase;
    private final RemoveOrganizationMemberUseCase removeOrganizationMemberUseCase;


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

    @GetMapping("/get-organization-branch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<GetOrganizationBranchResponse>> getOrganizationBranch(
        @RequestParam("organization_branch_id")
        @NotNull(message = "Organization branch id is required") Long organizationBranchId
    ) {
        GetOrganizationBranchResponse response = toGetOrganizationBranchResponse(
            getOrganizationBranchUseCase.execute(organizationBranchId)
        );

        return ResponseEntity.ok(
            SuccessResponse.success("Get organization branch successfully", response)
        );
    }

    @PostMapping("/create-organization-branch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<CreateOrganizationBranchResponse>> createOrganizationBranch(
        @RequestParam("organization_id")
        @NotNull(message = "Organization id is required") Long organizationId,
        @Valid @RequestBody CreateOrganizationBranchRequest request
    ) {
        CreateOrganizationBranchResponse response = toCreateOrganizationBranchResponse(
            createOrganizationBranchUseCase.execute(organizationId, request)
        );

        return ResponseEntity.ok(
            SuccessResponse.success("Create organization branch successfully", response)
        );
    }

    @PutMapping("/update-organization-branch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<UpdateOrganizationBranchResponse>> updateOrganizationBranch(
        @RequestParam("organization_branch_id")
        @NotNull(message = "Organization branch id is required") Long organizationBranchId,
        @Valid @RequestBody UpdateOrganizationBranchRequest request
    ) {
        UpdateOrganizationBranchResponse response = toUpdateOrganizationBranchResponse(
            updateOrganizationBranchUseCase.execute(organizationBranchId, request)
        );

        return ResponseEntity.ok(
            SuccessResponse.success("Update organization branch successfully", response)
        );
    }

    @DeleteMapping("/delete-organization-branch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Void>> deleteOrganizationBranch(
        @RequestParam("organization_branch_id")
        @NotNull(message = "Organization branch id is required") Long organizationBranchId
    ) {
        deleteOrganizationBranchUseCase.execute(organizationBranchId);

        return ResponseEntity.ok(
            SuccessResponse.successMessage("Delete organization branch successfully")
        );
    }
    

    // ============================== Organization Member =========================================
    @GetMapping("/get-organization-members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<GetOrganizationMembersResponse>>> getOrganizationMembers(
        @RequestParam("organization_branch_id")
        @NotNull(message = "Organization branch id is required") Long organizationBranchId
    ) {
        List<GetOrganizationMembersResponse> response = getOrganizationMembersUseCase.execute(organizationBranchId)
            .stream()
            .map(this::toGetOrganizationMembersResponse)
            .toList();

        return ResponseEntity.ok(
            SuccessResponse.success("Get organization members successfully", response)
        );
    }

    @PostMapping("/add-organization-member")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<AddOrganizationMemberResponse>> addOrganizationMember(
        @RequestParam("organization_branch_id")
        @NotNull(message = "Organization branch id is required") Long organizationBranchId,
        @Valid @RequestBody AddOrganizationMemberRequest request
    ) {
        AddOrganizationMemberResponse response = toAddOrganizationMemberResponse(
            addOrganizationMemberUseCase.execute(organizationBranchId, request)
        );

        return ResponseEntity.ok(
            SuccessResponse.success("Add organization member successfully", response)
        );
    }

    @PutMapping("/update-organization-member")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<UpdateOrganizationMemberResponse>> updateOrganizationMember(
        @RequestParam("member_id")
        @NotNull(message = "Member id is required") Long memberId,
        @Valid @RequestBody UpdateOrganizationMemberRequest request
    ) {
        UpdateOrganizationMemberResponse response = toUpdateOrganizationMemberResponse(
            updateOrganizationMemberUseCase.execute(memberId, request)
        );

        return ResponseEntity.ok(
            SuccessResponse.success("Update organization member successfully", response)
        );
    }

    @DeleteMapping("/remove-organization-member")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Void>> removeOrganizationMember(
        @RequestParam("member_id")
        @NotNull(message = "Member id is required") Long memberId
    ) {
        removeOrganizationMemberUseCase.execute(memberId);

        return ResponseEntity.ok(
            SuccessResponse.successMessage("Remove organization member successfully")
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

    private GetOrganizationBranchResponse toGetOrganizationBranchResponse(GetOrganizationBranchResult result) {
        return new GetOrganizationBranchResponse(
            result.id(),
            result.country(),
            result.address(),
            result.branchType(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    private CreateOrganizationBranchResponse toCreateOrganizationBranchResponse(CreateOrganizationBranchResult result) {
        return new CreateOrganizationBranchResponse(
            result.id(),
            result.country(),
            result.address(),
            result.branchType(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    private UpdateOrganizationBranchResponse toUpdateOrganizationBranchResponse(UpdateOrganizationBranchResult result) {
        return new UpdateOrganizationBranchResponse(
            result.id(),
            result.country(),
            result.address(),
            result.branchType(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    private GetOrganizationMembersResponse toGetOrganizationMembersResponse(GetOrganizationMembersResult result) {
        return new GetOrganizationMembersResponse(
            result.userId(),
            result.memberId(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            result.organizationRole(),
            result.organizationDepartment(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    private AddOrganizationMemberResponse toAddOrganizationMemberResponse(AddOrganizationMemberResult result) {
        return new AddOrganizationMemberResponse(
            result.userId(),
            result.memberId(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            result.organizationRole(),
            result.organizationDepartment(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    private UpdateOrganizationMemberResponse toUpdateOrganizationMemberResponse(UpdateOrganizationMemberResult result) {
        return new UpdateOrganizationMemberResponse(
            result.userId(),
            result.memberId(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            result.organizationRole(),
            result.organizationDepartment(),
            result.createdAt(),
            result.updatedAt()
        );
    }

}
