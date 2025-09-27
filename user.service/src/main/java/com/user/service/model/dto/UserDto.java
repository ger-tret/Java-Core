package com.user.service.model.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Id
    @NonNull
    private long userId;
    @NotNull(message = "Name is required")
    @Size(min = 1, max = 300)
    private String name;
    @NotNull(message = "Surname is required")
    @Size(min = 1, max = 300)
    private String surname;
    @NotNull(message = "Birthdate is required")
    @Size(min = 1, max = 300)
    private Date birthDate;
    @NonNull
    private String email;
    private List<CardMetadataDto> cardsDtos;

}
