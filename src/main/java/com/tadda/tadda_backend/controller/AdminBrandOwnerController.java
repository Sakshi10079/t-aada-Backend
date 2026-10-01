package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.dto.AdminBrandOwnerResponseDto;
import com.tadda.tadda_backend.service.BrandOwnerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/brand-owners")
@RequiredArgsConstructor
public class AdminBrandOwnerController {

    private final BrandOwnerProfileService brandOwnerProfileService;

    @GetMapping
    public ResponseEntity<List<AdminBrandOwnerResponseDto>> getAllBrandOwners() {
        return ResponseEntity.ok(
                brandOwnerProfileService.getAllBrandOwnersForAdmin()
        );
    }
}