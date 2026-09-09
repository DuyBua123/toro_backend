package com.toro.backend.infrastructure.database.models;

import java.time.Instant;

import com.toro.backend.infrastructure.database.enums.OrganizationDepartment;
import com.toro.backend.infrastructure.database.enums.OrganizationRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table (name = "organization_members")
public class OrganizationMember {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column()    
    private OrganizationRole organizationRole;

    @Enumerated(EnumType.STRING)
    @Column()    
    private OrganizationDepartment organizationDepartment;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, updatable = true)
    private Instant updatedAt;


    // Relationships
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_branch_id", nullable = false)
    private OrganizationBranch organizationBranch;

    
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();;
    }

    @PreUpdate 
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

}
