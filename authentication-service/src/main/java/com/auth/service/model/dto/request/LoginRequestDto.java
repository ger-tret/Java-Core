package com.auth.service.model.dto.request;

import com.auth.service.model.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;


@Data
public class LoginRequestDto {
    @NotBlank
    private String login;

    @NotBlank
    private String password;

}
