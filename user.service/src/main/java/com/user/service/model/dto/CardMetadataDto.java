package com.user.service.model.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardMetadataDto {

    @Id
    private long cardId;

    @NonNull
    private long userId;

    @NonNull
    private long cardNumber;

    @NonNull
    private String holder;

    @NonNull
    private Date experationDate;


}
