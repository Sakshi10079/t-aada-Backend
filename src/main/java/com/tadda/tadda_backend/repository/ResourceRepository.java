package com.tadda.tadda_backend.repository;

import com.tadda.tadda_backend.entity.ContentStatus;
import com.tadda.tadda_backend.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByStatus(ContentStatus status);
}
