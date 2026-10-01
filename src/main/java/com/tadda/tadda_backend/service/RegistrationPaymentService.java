package com.tadda.tadda_backend.service;
import com.tadda.tadda_backend.dto.AdminRegistrationPaymentResponseDto;
import com.tadda.tadda_backend.entity.PaymentStatus;
import com.tadda.tadda_backend.entity.RegistrationPayment;
import com.tadda.tadda_backend.entity.User;
import com.tadda.tadda_backend.repository.RegistrationPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationPaymentService {

    private final RegistrationPaymentRepository repository;

    public RegistrationPayment create(RegistrationPayment payment) {
        return repository.save(payment);
    }

    public List<RegistrationPayment> getAll() {
        return repository.findAll();
    }

    public RegistrationPayment getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<AdminRegistrationPaymentResponseDto> getAllPaymentsForAdmin() {
        return repository.findAllPaymentsForAdmin();
    }

    public RegistrationPayment markAsPaid(
            String razorpayOrderId,
            String razorpayPaymentId,
            User user
    ) {

        RegistrationPayment payment = repository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Registration payment not found"
                        )
                );

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Registration payment does not belong to this user"
            );
        }

        if (payment.getStatus() == PaymentStatus.PAID) {
            return payment;
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionId(razorpayPaymentId);
        payment.setPaymentMethod("RAZORPAY");
        payment.setPaidAt(LocalDateTime.now());

        return repository.save(payment);
    }

    public RegistrationPayment getByUser(User user) {
        return repository.findByUser(user)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Registration payment not found"
                        )
                );
    }
}