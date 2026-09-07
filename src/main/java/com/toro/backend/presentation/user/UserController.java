package com.toro.backend.presentation.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toro.backend.application.user.get_user.GetUserResult;
import com.toro.backend.application.user.get_user.GetUserUseCase;
import com.toro.backend.application.user.get_users.GetUsersResult;
import com.toro.backend.application.user.get_users.GetUsersUseCase;
import com.toro.backend.infrastructure.api.SuccessResponse;
import com.toro.backend.presentation.user.response.GetUserResponse;
import com.toro.backend.presentation.user.response.GetUsersResponse;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController 
@RequestMapping ("/api/users")
@RequiredArgsConstructor 
public class UserController {

    private final GetUsersUseCase getUsersUseCase;
    private final GetUserUseCase getUserUseCase;


    @GetMapping("/get-users")
    // @PreAuthorize("ADMIN")
    public ResponseEntity<SuccessResponse<List<GetUsersResponse>>> getUsers() {
        List<GetUsersResponse> response = getUsersUseCase.execute()
            .stream()
            .map(this::toGetUsersResponse)
            .toList();

        return ResponseEntity.ok(
            SuccessResponse.success("Get users successfully", response)
        );
    }

    @GetMapping("/get-user/{user_id}")
    // @PreAuthorize("ADMIN")
    public ResponseEntity<SuccessResponse<GetUserResponse>> getUser(
        @PathVariable("user_id") 
        @NotNull(message = "User id is required") Long userId
    ) {
        GetUserResponse response = toGetUserResponse(getUserUseCase.execute(userId));

        return ResponseEntity.ok(
            SuccessResponse.success("Get user successfully", response)
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
    

}
