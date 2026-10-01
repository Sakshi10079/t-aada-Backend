package com.tadda.tadda_backend.security;

import com.tadda.tadda_backend.entity.PaymentStatus;
import com.tadda.tadda_backend.entity.User;
import com.tadda.tadda_backend.repository.RegistrationPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandOwnerAuthorizationService {

    private final RegistrationPaymentRepository registrationPaymentRepository;

    public boolean isPaidBrandOwner(User user) {

        if (user.getRole() != com.tadda.tadda_backend.entity.Role.BRAND_OWNER) {
            return false;
        }

        return registrationPaymentRepository
                .findByUser(user)
                .map(payment -> payment.getStatus() == PaymentStatus.PAID)
                .orElse(false);
    }
}