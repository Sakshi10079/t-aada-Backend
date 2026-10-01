package com.tadda.tadda_backend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brand_owner_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandOwnerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String brandName;

    private String businessStage;

    @Column(columnDefinition = "TEXT")
    private String productCategories;

    @Column(columnDefinition = "TEXT")
    private String sellingPlatforms;

    @Column(columnDefinition = "TEXT")
    private String socialMediaHandles;

    private String mockupExperience;

    private String mockupStyle;
}