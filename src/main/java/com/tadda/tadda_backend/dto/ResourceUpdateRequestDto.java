package com.tadda.tadda_backend.dto;

import com.tadda.tadda_backend.entity.ContentStatus;

public record ResourceUpdateRequestDto(
        String title,
        String description,
        String type,
        String category,
        String fileUrl,
        ContentStatus status
) {
}