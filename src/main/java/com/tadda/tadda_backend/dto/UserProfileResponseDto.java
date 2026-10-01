package com.tadda.tadda_backend.dto;

public record UserProfileResponseDto(
        Long id,
        String name,
        String email,
        String phone
) {
}
