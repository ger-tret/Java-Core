package com.api.gateway.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequestDto {

    @Size(min = 5, message = "Minimum login length is 5 characters.")
    private String login;

    @Size(min = 8, message = "Minimum password length is 8 characters.")
    private String password;

    @NotBlank(message = "First name can't be blank.")
    private String firstName;

    @NotBlank(message = "Last name can't be blank.")
    private String lastName;

    @NotBlank(message = "Email can't be blank.")
    @Email
    private String email;
}