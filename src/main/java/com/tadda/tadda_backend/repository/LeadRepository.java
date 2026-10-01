package com.tadda.tadda_backend.repository;

import com.tadda.tadda_backend.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
