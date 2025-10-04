package com.auth.service.model.dto.request;

import com.auth.service.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

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

    @NotBlank(message = "Roles can't be blank.")
    private List<UserRole> roles;

}
