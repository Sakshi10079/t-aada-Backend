package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.entity.Lead;
import com.tadda.tadda_backend.entity.LeadStatus;
import com.tadda.tadda_backend.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        Lead createdLead = leadService.create(lead);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLead);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Lead>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Lead> getLeadById(@PathVariable Long id) {
        Lead lead = leadService.getById(id);

        if (lead == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lead);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Lead> updateLeadStatus(
            @PathVariable Long id,
            @RequestParam LeadStatus status
    ) {
        Lead updatedLead = leadService.updateStatus(id, status);

        if (updatedLead == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedLead);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        leadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}