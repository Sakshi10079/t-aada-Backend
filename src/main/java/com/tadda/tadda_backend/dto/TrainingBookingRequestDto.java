package com.tadda.tadda_backend.dto;

import java.time.LocalDate;

public record TrainingBookingRequestDto(
        String name,
        String email,
        String phone,
        LocalDate trainingDate
) {
}