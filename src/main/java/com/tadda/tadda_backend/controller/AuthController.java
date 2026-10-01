package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.dto.LoginRequestDto;
import com.tadda.tadda_backend.dto.LoginResponseDto;
import com.tadda.tadda_backend.dto.RegisterRequestDto;
import com.tadda.tadda_backend.dto.RegisterResponseDto;
import com.tadda.tadda_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}