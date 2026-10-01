package com.tadda.tadda_backend.repository;

import com.tadda.tadda_backend.dto.AdminRegistrationPaymentResponseDto;
import com.tadda.tadda_backend.entity.RegistrationPayment;
import com.tadda.tadda_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegistrationPaymentRepository extends JpaRepository<RegistrationPayment, Long> {

    @Query("""
            SELECT new com.tadda.tadda_backend.dto.AdminRegistrationPaymentResponse(
                p.id,
                u.name,
                u.email,
                p.amount,
                p.paymentMethod,
                p.transactionId,
                p.status,
                p.paidAt,
                p.createdAt
            )
            FROM RegistrationPayment p
            JOIN p.user u
            """)
    List<AdminRegistrationPaymentResponseDto> findAllPaymentsForAdmin();

    Optional<RegistrationPayment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<RegistrationPayment> findByUser(User user);
}