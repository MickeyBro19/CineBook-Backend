package com.mickey.supportdesk.service;

import com.mickey.supportdesk.dto.UserRequestDto;
import com.mickey.supportdesk.dto.UserResponseDto;

import java.util.List;

public interface UserService {
     List<UserResponseDto> findAllUsers();
    UserResponseDto registerUser(UserRequestDto user);
    UserResponseDto findByEmail(String email);
    void deleteUser(Long id, String email);
    UserResponseDto updateUser(UserRequestDto user);
}
