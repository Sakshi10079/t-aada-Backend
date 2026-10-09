package com.tadda.tadda_backend.service;

import com.tadda.tadda_backend.dto.AdminTrainingBookingResponseDto;
import com.tadda.tadda_backend.entity.TrainingBooking;
import com.tadda.tadda_backend.entity.TrainingBookingStatus;
import com.tadda.tadda_backend.entity.TrainingPaymentStatus;
import com.tadda.tadda_backend.repository.TrainingBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingBookingService {

    private static final double TRAINING_FEE = 99.0;

    private final TrainingBookingRepository trainingBookingRepository;

    public TrainingBooking createBooking(
            String name,
            String email,
            String phone,
            LocalDate trainingDate
    ) {

        TrainingBooking booking = TrainingBooking.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .trainingDate(trainingDate)
                .amount(TRAINING_FEE)
                .paymentStatus(TrainingPaymentStatus.PENDING)
                .bookingStatus(TrainingBookingStatus.PENDING)
                .build();

        return trainingBookingRepository.save(booking);
    }

    public TrainingBooking getByRazorpayOrderId(
            String razorpayOrderId
    ) {
        return trainingBookingRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElse(null);
    }

    public TrainingBooking markAsPaid(
            String razorpayOrderId,
            String razorpayPaymentId
    ) {

        TrainingBooking booking = trainingBookingRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Training booking not found"
                        )
                );

        if (booking.getPaymentStatus()
                == TrainingPaymentStatus.PAID) {
            return booking;
        }

        booking.setPaymentStatus(
                TrainingPaymentStatus.PAID
        );

        booking.setBookingStatus(
                TrainingBookingStatus.CONFIRMED
        );

        booking.setTransactionId(
                razorpayPaymentId
        );

        booking.setPaymentMethod(
                "RAZORPAY"
        );

        return trainingBookingRepository.save(booking);
    }

    public List<AdminTrainingBookingResponseDto>
    getAllBookingsForAdmin() {

        return trainingBookingRepository
                .findAllBookingsForAdmin();
    }
}