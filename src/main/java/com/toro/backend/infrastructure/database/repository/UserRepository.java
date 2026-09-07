package com.toro.backend.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toro.backend.infrastructure.database.enums.AppRole;
import com.toro.backend.infrastructure.database.models.User;

import java.util.List;
import java.util.Optional;



public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    List<User> findByRoleNot(AppRole role);


    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

}
