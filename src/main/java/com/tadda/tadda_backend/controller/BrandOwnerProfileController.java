package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.entity.BrandOwnerProfile;
import com.tadda.tadda_backend.entity.User;
import com.tadda.tadda_backend.service.BrandOwnerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brand-owner/profile")
@RequiredArgsConstructor
public class BrandOwnerProfileController {

    private final BrandOwnerProfileService profileService;

    @GetMapping
    @PreAuthorize("@brandOwnerAuthorizationService.isPaidBrandOwner(authentication.principal)")
    public ResponseEntity<BrandOwnerProfile> getMyProfile(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        BrandOwnerProfile profile = profileService.getByUser(user);

        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(profile);
    }

    @PutMapping
    @PreAuthorize("@brandOwnerAuthorizationService.isPaidBrandOwner(authentication.principal)")
    public ResponseEntity<BrandOwnerProfile> updateMyProfile(
            @RequestBody BrandOwnerProfile updatedProfile,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        BrandOwnerProfile updated = profileService.updateByUser(
                user,
                updatedProfile
        );

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }
}