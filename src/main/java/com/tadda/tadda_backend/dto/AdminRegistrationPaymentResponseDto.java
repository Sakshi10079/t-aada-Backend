package com.tadda.tadda_backend.dto;

import com.tadda.tadda_backend.entity.PaymentStatus;

import java.time.LocalDateTime;

public record AdminRegistrationPaymentResponseDto(
        Long id,
        String name,
        String email,
        Double amount,
        String paymentMethod,
        String transactionId,
        PaymentStatus status,
        LocalDateTime paidAt,
        LocalDateTime createdAt
) {
}