package com.tadda.tadda_backend.service;
import com.tadda.tadda_backend.entity.Lead;
import com.tadda.tadda_backend.entity.LeadStatus;
import com.tadda.tadda_backend.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository repository;

    public Lead create(Lead lead) {
        lead.setStatus(LeadStatus.NEW);
//        lead.setCreatedAt(LocalDateTime.now());
        return repository.save(lead);
    }

    public List<Lead> getAll() {
        return repository.findAll();
    }

    public Lead getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Lead updateStatus(Long id, LeadStatus status) {
        Lead lead = repository.findById(id).orElse(null);

        if (lead == null) {
            return null;
        }

        lead.setStatus(status);

        return repository.save(lead);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}