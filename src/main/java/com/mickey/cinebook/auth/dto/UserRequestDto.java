package com.mickey.cinebook.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequestDto{
        @NotBlank
        @Size(min = 3, max = 50)
        String name;
        @NotBlank
        @Email
        String email;
        @NotBlank
        String password;
}
