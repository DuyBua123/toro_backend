package com.toro.backend.application.user.get_users;

import java.util.List;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class GetUsersUseCase {

    private final UserRepository userRepository;

    public List<GetUsersResult> execute() {
        return userRepository.findAll()
            .stream()
            .map(this::toResult)
            .toList();
    }


    // PRIVATE METHODS
    private GetUsersResult toResult(User user) {
        return new GetUsersResult(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.isActive(),
            user.getCreatedAt()
        );
    }

}
