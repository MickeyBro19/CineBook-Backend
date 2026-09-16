package com.mickey.cinebook.auth.service;

import com.mickey.cinebook.dto.UserRequestDto;
import com.mickey.cinebook.dto.UserResponseDto;

import java.util.List;

public interface UserService {
     List<UserResponseDto> findAllUsers();
    UserResponseDto registerUser(UserRequestDto user);
    UserResponseDto findByEmail(String email);
    void deleteUser(Long id, String email);
    UserResponseDto updateUser(UserRequestDto user);
}
