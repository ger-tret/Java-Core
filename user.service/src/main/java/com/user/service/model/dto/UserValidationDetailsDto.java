package com.user.service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserValidationDetailsDto {
    private long userId;
    private String name;
    private String surname;
    private Date birthDate;
    private String email;

}
