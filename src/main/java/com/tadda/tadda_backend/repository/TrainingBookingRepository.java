package com.tadda.tadda_backend.repository;

import com.tadda.tadda_backend.dto.AdminTrainingBookingResponseDto;
import com.tadda.tadda_backend.entity.TrainingBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainingBookingRepository
        extends JpaRepository<TrainingBooking, Long> {

    Optional<TrainingBooking> findByRazorpayOrderId(String razorpayOrderId);

    @Query("""
            SELECT new com.tadda.tadda_backend.dto.AdminTrainingBookingResponseDto(
                b.id,
                b.name,
                b.email,
                b.phone,
                b.trainingDate,
                b.amount,
                b.transactionId,
                b.paymentStatus,
                b.bookingStatus,
                b.createdAt
            )
            FROM TrainingBooking b
            ORDER BY b.createdAt DESC
            """)
    List<AdminTrainingBookingResponseDto> findAllBookingsForAdmin();
}