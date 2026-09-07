package com.toro.backend.application.user.get_user;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.User;

import lombok.RequiredArgsConstructor;


@Service 
@RequiredArgsConstructor 
public class GetUserUseCase {

    private final GetUserValidator getUserValidator;


    public GetUserResult execute(Long userId) {

        User user = getUserValidator.validate(userId);

        return toResult(user);
    }


    // PRIVATE METHODS
    private GetUserResult toResult(User user) {
        return new GetUserResult(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.isActive(),
            user.getCreatedAt()
        );
    }

}
