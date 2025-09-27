package com.user.service.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Entity
@Table(name = "cards_info")
@Data
@AllArgsConstructor
@Builder
public class CardMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    @SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq", allocationSize = 1)
    private long cardId;

    @NonNull
    private long userId;

    private long cardNumber;

    private String holder;

    private Date experationDate;

}
