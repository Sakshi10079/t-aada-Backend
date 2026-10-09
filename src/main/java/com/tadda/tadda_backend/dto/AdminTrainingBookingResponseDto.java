package com.tadda.tadda_backend.dto;

import com.tadda.tadda_backend.entity.TrainingBookingStatus;
import com.tadda.tadda_backend.entity.TrainingPaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminTrainingBookingResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        LocalDate trainingDate,
        Double amount,
        String transactionId,
        TrainingPaymentStatus paymentStatus,
        TrainingBookingStatus bookingStatus,
        LocalDateTime createdAt
) {
}