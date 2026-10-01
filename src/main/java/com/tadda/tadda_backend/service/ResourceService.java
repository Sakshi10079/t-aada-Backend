package com.tadda.tadda_backend.service;
import com.tadda.tadda_backend.dto.ResourceUpdateRequestDto;
import com.tadda.tadda_backend.entity.ContentStatus;
import com.tadda.tadda_backend.entity.Resource;
import com.tadda.tadda_backend.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository repository;

    public Resource create(Resource resource) {
        return repository.save(resource);
    }

    public List<Resource> getAll() {
        return repository.findAll();
    }

    public Resource getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Resource update(Long id, ResourceUpdateRequestDto request) {

        Resource existingResource = repository.findById(id).orElse(null);

        if (existingResource == null) {
            return null;
        }

        existingResource.setTitle(request.title());
        existingResource.setDescription(request.description());
        existingResource.setType(request.type());
        existingResource.setCategory(request.category());
        existingResource.setFileUrl(request.fileUrl());
        existingResource.setStatus(request.status());

        return repository.save(existingResource);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Resource> getPublishedResources() {
        return repository.findByStatus(ContentStatus.PUBLISHED);
    }
}