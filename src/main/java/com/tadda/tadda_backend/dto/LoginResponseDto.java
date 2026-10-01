package com.tadda.tadda_backend.dto;

public record LoginResponseDto(
        Long userId,
        String name,
        String email,
        String role,
        String token,
        String message
) {
}
