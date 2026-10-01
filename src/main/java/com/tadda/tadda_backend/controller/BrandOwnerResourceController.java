package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.entity.Resource;
import com.tadda.tadda_backend.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand-owner/resources")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BRAND_OWNER')")
public class BrandOwnerResourceController {

    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<List<Resource>> getPublishedResources() {
        return ResponseEntity.ok(resourceService.getPublishedResources());
    }
}