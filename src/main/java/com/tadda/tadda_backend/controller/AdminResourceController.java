package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.dto.ResourceUpdateRequestDto;
import com.tadda.tadda_backend.entity.Resource;
import com.tadda.tadda_backend.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/resources")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminResourceController {

    private final ResourceService resourceService;

    @PostMapping
    public ResponseEntity<Resource> createResource(
            @RequestBody Resource resource
    ) {
        Resource createdResource = resourceService.create(resource);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdResource);
    }

    @GetMapping
    public ResponseEntity<List<Resource>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(
            @PathVariable Long id
    ) {
        Resource resource = resourceService.getById(id);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource(
            @PathVariable Long id,
            @RequestBody ResourceUpdateRequestDto request
    ) {
        Resource updatedResource = resourceService.update(id, request);

        if (updatedResource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(
            @PathVariable Long id
    ) {
        Resource existingResource = resourceService.getById(id);

        if (existingResource == null) {
            return ResponseEntity.notFound().build();
        }

        resourceService.delete(id);

        return ResponseEntity.noContent().build();
    }
}