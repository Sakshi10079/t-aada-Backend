package com.tadda.tadda_backend.service;

import com.tadda.tadda_backend.dto.ChangePasswordRequestDto;
import com.tadda.tadda_backend.dto.UpdateUserProfileRequestDto;
import com.tadda.tadda_backend.dto.UserProfileResponseDto;
import com.tadda.tadda_backend.entity.User;
import com.tadda.tadda_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserProfileResponseDto getMyProfile(User user) {
        return new UserProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public UserProfileResponseDto updateMyProfile(
            User user,
            UpdateUserProfileRequestDto request
    ) {
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());

        User updatedUser = userRepository.save(user);

        return new UserProfileResponseDto(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhone()
        );
    }

    public void changePassword(
            User user,
            ChangePasswordRequestDto request
    ) {
        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        user.setPassword(
                passwordEncoder.encode(request.newPassword())
        );

        userRepository.save(user);
    }
}