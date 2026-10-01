package com.tadda.tadda_backend.dto;

import com.tadda.tadda_backend.entity.Status;

import java.time.LocalDateTime;

public record AdminBrandOwnerResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        Status status,
        String brandName,
        String businessStage,
        LocalDateTime createdAt
) {
}