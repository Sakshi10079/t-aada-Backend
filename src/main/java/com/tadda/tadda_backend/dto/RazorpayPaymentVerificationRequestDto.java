package com.tadda.tadda_backend.dto;

public record RazorpayPaymentVerificationRequestDto(
        String razorpayPaymentId,
        String razorpayOrderId,
        String razorpaySignature
) {
}