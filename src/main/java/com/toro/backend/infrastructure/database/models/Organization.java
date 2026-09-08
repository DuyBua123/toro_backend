package com.toro.backend.infrastructure.database.models;

import java.time.Instant;
import java.time.LocalDateTime;

import com.toro.backend.infrastructure.database.enums.OrganizationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity 
@Table (name = "organizations")
public class Organization {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_code", nullable = false, unique = true)
    private String organizationCode;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type", nullable = false)
    private OrganizationType organizationType;

    // @Column(name = "country")
    // private String country;

    // @Column(name = "address", columnDefinition = "TEXT")
    // private String address;

    @Column(name = "tax_code", unique = true)
    private String taxCode;

    @Column(name = "blockchain_wallet")
    private String blockchainWallet;

    // @Column(name = "trust_score", precision = 10, scale = 2)
    // private BigDecimal trustScore;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private Instant updatedAt;



    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate 
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
    
}
