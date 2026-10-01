package com.tadda.tadda_backend.dto;

public record ChangePasswordRequestDto(
        String currentPassword,
        String newPassword
) {
}