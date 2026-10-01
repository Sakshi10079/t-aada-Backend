package com.tadda.tadda_backend.dto;

public record UpdateUserProfileRequestDto(
        String name,
        String email,
        String phone
) {
}
