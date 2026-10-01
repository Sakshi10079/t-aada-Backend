package com.tadda.tadda_backend.service;

import com.tadda.tadda_backend.dto.LoginRequestDto;
import com.tadda.tadda_backend.dto.LoginResponseDto;
import com.tadda.tadda_backend.dto.RegisterRequestDto;
import com.tadda.tadda_backend.dto.RegisterResponseDto;
import com.tadda.tadda_backend.entity.*;
import com.tadda.tadda_backend.exception.EmailAlreadyExistsException;
import com.tadda.tadda_backend.repository.*;
import com.tadda.tadda_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BrandOwnerProfileRepository brandOwnerProfileRepository;
    private final RegistrationPaymentRepository registrationPaymentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.BRAND_OWNER)
                .status(Status.ACTIVE)
                .build();

        userRepository.save(user);

        BrandOwnerProfile profile = BrandOwnerProfile.builder()
                .user(user)
                .brandName(request.brandName())
                .businessStage(request.businessStage())
                .productCategories(request.productCategories())
                .sellingPlatforms(request.sellingPlatforms())
                .socialMediaHandles(request.socialMediaHandles())
                .mockupExperience(request.mockupExperience())
                .mockupStyle(request.mockupStyle())
                .build();

        brandOwnerProfileRepository.save(profile);

        RegistrationPayment payment = RegistrationPayment.builder()
                .user(user)
                .amount(1799.0)
                .status(PaymentStatus.PENDING)
                .build();

        registrationPaymentRepository.save(payment);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new RegisterResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                token,
                "Registration successful"
        );
    }

    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                token,
                "Login successful"
        );
    }
}