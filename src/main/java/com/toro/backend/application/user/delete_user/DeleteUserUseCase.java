package com.toro.backend.application.user.delete_user;

import org.springframework.stereotype.Service;

import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final DeleteUserValidator deleteUserValidator;
    private final UserRepository userRepository;


    @Transactional
    public void execute(Long userId) {
        User user = deleteUserValidator.validate(userId);

        userRepository.delete(user);
    }

}
