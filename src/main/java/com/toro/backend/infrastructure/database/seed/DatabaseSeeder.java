package com.toro.backend.infrastructure.database.seed;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.toro.backend.infrastructure.database.models.User;
import com.toro.backend.infrastructure.database.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    
    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Seeder running on startup...");

        cleanUp();
        seedAccounts();

    }


    // PRIVATE METHODS
    @Transactional
    private void cleanUp() {
        List<String> tableNames = jdbcTemplate.queryForList("""
            SELECT tablename
            FROM pg_tables
            WHERE schemaname = 'public'
            AND tablename NOT IN (
                'flyway_schema_history',
                'databasechangelog',
                'databasechangeloglock'
            )
            """, String.class);

        if (tableNames.isEmpty()) {
            return;
        }

        String tables = tableNames.stream()
            .map(tableName -> "\"" + tableName.replace("\"", "\"\"") + "\"")
            .collect(Collectors.joining(", "));

        jdbcTemplate.execute(
            "TRUNCATE TABLE " + tables + " RESTART IDENTITY CASCADE"
        );

        System.out.println("All tables cleaned and sequences reset.");
    }

    private void seedAccounts() {

        try {
            User adminAccount = User.builder()
                    .fullName("System Administrator")
                    .email("admin@example.com")
                    .phoneNumber("0123456789")
                    .passwordHash(passwordEncoder.encode("123456"))
                    .status("ACTIVE")
                    .build();


            List<User> users = List.of(adminAccount);
            userRepository.saveAll(users);

            System.out.println("Users seeded successfully.");
        } catch (Exception ex) {
            System.err.println("Error seeding users: " + ex.getMessage());
        }

    }

}
