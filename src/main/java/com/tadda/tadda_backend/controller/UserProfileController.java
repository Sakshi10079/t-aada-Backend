package com.tadda.tadda_backend.controller;

import com.tadda.tadda_backend.dto.ChangePasswordRequestDto;
import com.tadda.tadda_backend.dto.UpdateUserProfileRequestDto;
import com.tadda.tadda_backend.dto.UserProfileResponseDto;
import com.tadda.tadda_backend.entity.User;
import com.tadda.tadda_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserProfileResponseDto> getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(userService.getMyProfile(user));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDto> updateMyProfile(
            @RequestBody UpdateUserProfileRequestDto request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                userService.updateMyProfile(user, request)
        );
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequestDto request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        userService.changePassword(user, request);

        return ResponseEntity.noContent().build();
    }
}