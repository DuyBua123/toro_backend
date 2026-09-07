package com.toro.backend.presentation.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
    @NotBlank 
    String fullName,
    @NotBlank 
    @Email 
    String email,
    @NotBlank 
    @Pattern (
        // Accept: 0xxxxxxxxx || +84xxxxxxxxx
        regexp = "^(?:\\+84|0)(?:3|5|7|8|9)[0-9]{8}$",
        message = "Phone number must be Vietnam"
    )
    String phoneNumber,
    @NotBlank 
    String password,
    @NotNull  
    boolean isActive
) {

}
