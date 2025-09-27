package com.user.service.service;

import com.user.service.model.dto.CardMetadataDto;
import com.user.service.model.entity.CardMetadata;

import java.util.List;

public interface CardService {
    Long createCard(CardMetadataDto cardMetadataDto);
    CardMetadataDto findCardMetadataById(Long id);
    List<CardMetadata> findCardsMetadatasByIdCsv(String idCsv);
    List<CardMetadata> getCardsByUserEmail(String email);
    Long updateCardMetadata(Long id, CardMetadataDto updatedCard);
    Long deleteCardMetadata(Long id);

}
