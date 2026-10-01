package com.tadda.tadda_backend.repository;

import com.tadda.tadda_backend.dto.AdminBrandOwnerResponseDto;
import com.tadda.tadda_backend.entity.BrandOwnerProfile;
import com.tadda.tadda_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandOwnerProfileRepository extends JpaRepository<BrandOwnerProfile, Long> {
    Optional<BrandOwnerProfile> findByUser(User user);

    @Query("""
        SELECT new com.tadda.tadda_backend.dto.AdminBrandOwnerResponse(
            u.id,
            u.name,
            u.email,
            u.phone,
            u.status,
            p.brandName,
            p.businessStage,
            u.createdAt
        )
        FROM BrandOwnerProfile p
        JOIN p.user u
        WHERE u.role = com.tadda.tadda_backend.entity.Role.BRAND_OWNER
        """)
    List<AdminBrandOwnerResponseDto> findAllBrandOwnersForAdmin();
}
