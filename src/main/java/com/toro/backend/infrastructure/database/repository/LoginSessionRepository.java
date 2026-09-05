package com.toro.backend.infrastructure.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toro.backend.infrastructure.database.models.LoginSession;

public interface LoginSessionRepository extends JpaRepository<LoginSession, Long> {

    Optional<LoginSession> findFirstByUserIdAndRevokedReasonIsNull(Long userId);

    Optional<LoginSession> findByHashedRefreshToken(String hashedRefreshToken);

}
