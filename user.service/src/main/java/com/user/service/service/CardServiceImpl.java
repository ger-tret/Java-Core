package com.user.service.service;

import com.user.service.model.dto.CardMetadataDto;
import com.user.service.model.entity.CardMetadata;
import com.user.service.repository.CardRepository;
import com.user.service.service.mapper.CardMetadataMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CardServiceImpl implements CardService {

    private final CardRepository repository;
    private final CardMetadataMapper mapper;


    @Transactional
    @Override
    @CachePut(value = "cards", key = "#customer.id")
    public Long createCard(CardMetadataDto cardMetadataDto) {
        return repository.save(mapper.toEntity(cardMetadataDto)).getCardId();
    }

    @Override
    @Cacheable(value = "cards", key = "#id")
    public CardMetadataDto findCardMetadataById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Card for ID= " + id + " not found")));
    }

    @Override
    @Cacheable(value = "cards", key = "#id")
    public List<CardMetadata> findCardsMetadatasByIdCsv(String idCsv) {
        List<Long> ids = Arrays.stream(idCsv.split(",")).map(Long::parseLong).toList();
        List<CardMetadata> foundCards = repository.findAllById(ids).stream().toList();
        return foundCards;
    }

    @Transactional
    @Override
    @CachePut(value = "cards", key = "#customer.id")
    public Long updateCardMetadata(Long id, CardMetadataDto updatedCard) {
        return repository.findById(id).map(card -> {
            card.setUserId(updatedCard.getUserId());
            card.setHolder(updatedCard.getHolder());
            card.setExperationDate(updatedCard.getExperationDate());
            return repository.save(card);
        }).orElseThrow(() -> new NoSuchElementException("Card for ID= " + id + " not found")).getCardId();
    }

    @Override
    @Cacheable(value = "cards")
    public List<CardMetadata> getCardsByUserEmail(String email){
        return repository.findCardsByUserEmail(email);
    }

    @Transactional
    @Override
    @CacheEvict(value = "cards", key = "#id")
    public Long deleteCardMetadata(Long id) {
        repository.deleteById(id);
        return id;
    }
}
