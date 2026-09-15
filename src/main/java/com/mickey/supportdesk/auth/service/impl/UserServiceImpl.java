package com.mickey.supportdesk.auth.service.impl;

import com.mickey.supportdesk.dto.UserRequestDto;
import com.mickey.supportdesk.dto.UserResponseDto;
import com.mickey.supportdesk.entity.User;
import com.mickey.supportdesk.exception.EmailAlreadyExistsException;
import com.mickey.supportdesk.repository.UserRepository;
import com.mickey.supportdesk.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public List<UserResponseDto> findAllUsers() {
		return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserResponseDto.class)).toList();
	}

	@Override
	public UserResponseDto registerUser(UserRequestDto user) {

		String email = user.getEmail().toLowerCase().trim();
		if (userRepository.existsByEmail(email)) {
			throw new EmailAlreadyExistsException("User with email: " + email + " already exists!");
		}
		String password = passwordEncoder.encode(user.getPassword());
		User newUser = User.builder().name(user.getName()).email(email).password(password).build();
		userRepository.save(newUser);
		return modelMapper.map(newUser, UserResponseDto.class);
	}

	@Override
	public UserResponseDto findByEmail(String email) {
		return null;
	}

	@Override
	public void deleteUser(Long id, String email) {

	}

	@Override
	public UserResponseDto updateUser(UserRequestDto user) {
		return null;
	}


}
