package com.toro.backend.presentation.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toro.backend.application.user.create_user.CreateUserResult;
import com.toro.backend.application.user.create_user.CreateUserUseCase;
import com.toro.backend.application.user.delete_user.DeleteUserUseCase;
import com.toro.backend.application.user.get_user.GetUserResult;
import com.toro.backend.application.user.get_user.GetUserUseCase;
import com.toro.backend.application.user.get_users.GetUsersResult;
import com.toro.backend.application.user.get_users.GetUsersUseCase;
import com.toro.backend.application.user.update_user.UpdateUserResult;
import com.toro.backend.application.user.update_user.UpdateUserUseCase;
import com.toro.backend.infrastructure.api.SuccessResponse;
import com.toro.backend.presentation.user.request.CreateUserRequest;
import com.toro.backend.presentation.user.request.UpdateUserRequest;
import com.toro.backend.presentation.user.response.CreateUserResponse;
import com.toro.backend.presentation.user.response.GetUserResponse;
import com.toro.backend.presentation.user.response.GetUsersResponse;
import com.toro.backend.presentation.user.response.UpdateUserResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;





@RestController 
@RequestMapping ("/api/users")
@RequiredArgsConstructor 
public class UserController {

    private final GetUsersUseCase getUsersUseCase;
    private final GetUserUseCase getUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;


    @GetMapping("/get-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<GetUsersResponse>>> getUsers() {
        List<GetUsersResponse> response = getUsersUseCase.execute()
            .stream()
            .map(this::toGetUsersResponse)
            .toList();

        return ResponseEntity.ok(
            SuccessResponse.success("Get users successfully", response)
        );
    }

    @GetMapping("/get-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<GetUserResponse>> getUser(
        @RequestParam("user_id") 
        @NotNull(message = "User id is required") Long userId
    ) {
        GetUserResponse response = toGetUserResponse(getUserUseCase.execute(userId));

        return ResponseEntity.ok(
            SuccessResponse.success("Get user successfully", response)
        );
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<CreateUserResponse>> createUser(
        @Valid @RequestBody CreateUserRequest request
    ) {
        CreateUserResponse response = toCreateUserResponse(createUserUseCase.execute(request));

        return ResponseEntity.ok(
            SuccessResponse.success("Create user successfully", response)
        );
    }
    
    @PutMapping("/update-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<UpdateUserResponse>> updateUser(
        @RequestParam("user_id") 
        @NotNull(message = "User id is required") Long userId,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        UpdateUserResponse response = toUpdateUserResponse(updateUserUseCase.execute(userId, request));

        return ResponseEntity.ok(
            SuccessResponse.success("Update user successfully", response)
        );
    }

    @DeleteMapping("/delete-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Void>> deleteUser(
        @RequestParam("user_id") 
        @NotNull(message = "User id is required") Long userId
    ) {
        deleteUserUseCase.execute(userId);

        return ResponseEntity.ok(
            SuccessResponse.successMessage("Delete user successfully")
        );
    }


    // PRIVATE METHODS
    private GetUsersResponse toGetUsersResponse(GetUsersResult result) {
        return new GetUsersResponse(
            result.id(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            result.isActive(),
            result.createdAt()
        );
    }

    private GetUserResponse toGetUserResponse(GetUserResult result) {
        return new GetUserResponse(
            result.id(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            result.isActive(),
            result.createdAt()
        );
    }

    private CreateUserResponse toCreateUserResponse(CreateUserResult result) {
        return new CreateUserResponse(
            result.id(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            result.isActive(),
            result.createdAt()
        );
    }

    private UpdateUserResponse toUpdateUserResponse(UpdateUserResult result) {
        return new UpdateUserResponse(
            result.id(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            result.isActive(),
            result.createdAt()
        );
    }
    

}
