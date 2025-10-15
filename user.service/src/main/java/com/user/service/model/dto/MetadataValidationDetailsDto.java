package com.user.service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MetadataValidationDetailsDto {
    private long cardId;
    private long userId;
    private long cardNumber;
    private String holder;
    private Date experationDate;
}
