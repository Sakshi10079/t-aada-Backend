package com.tadda.tadda_backend.dto;

public record LoginRequestDto(
        String email,
        String password
) {
}
