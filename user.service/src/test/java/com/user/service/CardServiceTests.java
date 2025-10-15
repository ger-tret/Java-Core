package com.user.service;

import com.user.service.model.dto.CardMetadataDto;
import com.user.service.model.entity.CardMetadata;
import com.user.service.model.entity.User;
import com.user.service.repository.CardRepository;
import com.user.service.service.CardServiceImpl;
import com.user.service.service.mapper.CardMetadataMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTests {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMetadataMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private CardMetadata testCard;
    private CardMetadataDto testCardDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "John", "Doe", new Date(1990, 1, 1), "john@example.com", new ArrayList<>());
        testCard = new CardMetadata(1L, testUser.getUserId(), 4111111111111111L, "JOHN DOE", new Date(2025, 12, 31));
        testCardDto = new CardMetadataDto(1L, 1L, 4111111111111111l, "JOHN DOE", new Date(2025, 12, 31));
    }

    @Test
    void createCard_ShouldReturnCardId_WhenValidInput() {
         
        when(cardMapper.toEntity(testCardDto)).thenReturn(testCard);
        when(cardRepository.save(testCard)).thenReturn(testCard);

         
        Long result = cardService.createCard(testCardDto);

         
        assertEquals(1L, result);
        verify(cardRepository).save(testCard);
    }

    @Test
    void findCardMetadataById_ShouldReturnCardDto_WhenCardExists() {
         
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardMapper.toDto(testCard)).thenReturn(testCardDto);

         
        CardMetadataDto result = cardService.findCardMetadataById(1L);

         
        assertNotNull(result);
        assertEquals(1L, result.getCardId());
        verify(cardRepository).findById(1L);
    }

    @Test
    void findCardMetadataById_ShouldThrowException_WhenCardNotFound() {
         
        when(cardRepository.findById(999L)).thenReturn(Optional.empty());


        assertThrows(NoSuchElementException.class, () -> cardService.findCardMetadataById(999L));
    }

    @Test
    void findCardsMetadatasByIdCsv_ShouldReturnCards_WhenIdsExist() {
         
        CardMetadata card2 = new CardMetadata(2L, testUser.getUserId(), 4222222222222222L, "JOHN DOE", new Date(2026, 6, 30));
        CardMetadata card3 = new CardMetadata(3L, testUser.getUserId(), 4333333333333333L, "JOHN DOE", new Date(2024, 3, 15));

        when(cardRepository.findAllById(Arrays.asList(1L, 2L, 3L)))
                .thenReturn(Arrays.asList(testCard, card2, card3));

         
        List<CardMetadata> result = cardService.findCardsMetadatasByIdCsv("1,2,3");

        assertEquals(3, result.size());
        verify(cardRepository).findAllById(Arrays.asList(1L, 2L, 3L));
    }

    @Test
    void getCardsByUserEmail_ShouldReturnCards_WhenEmailExists() {
        when(cardRepository.findCardsByUserEmail("john@example.com"))
                .thenReturn(Arrays.asList(testCard));

        List<CardMetadata> result = cardService.getCardsByUserEmail("john@example.com");

        assertEquals(1, result.size());
        assertEquals(4111111111111111L, result.get(0).getCardNumber());
        verify(cardRepository).findCardsByUserEmail("john@example.com");
    }

    @Test
    void updateCardMetadata_ShouldUpdateAndReturnId_WhenCardExists() {
        CardMetadataDto updatedDto = new CardMetadataDto(1L, 1L, 4111111111111111L, "JOHN M. DOE", new Date(2026, 12, 31));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(testCard)).thenReturn(testCard);

         
        Long result = cardService.updateCardMetadata(1L, updatedDto);

         
        assertEquals(1L, result);
        assertEquals("JOHN M. DOE", testCard.getHolder());
        verify(cardRepository).save(testCard);
    }

    @Test
    void deleteCardMetadata_ShouldReturnId_WhenCardDeleted() {
         
        doNothing().when(cardRepository).deleteById(1L);

         
        Long result = cardService.deleteCardMetadata(1L);

         
        assertEquals(1L, result);
        verify(cardRepository).deleteById(1L);
    }


    @Test
    void getCardsByUserEmail_ShouldReturnEmptyList_WhenNoCardsFound() {
         
        when(cardRepository.findCardsByUserEmail("unknown@example.com"))
                .thenReturn(Collections.emptyList());

         
        List<CardMetadata> result = cardService.getCardsByUserEmail("unknown@example.com");

         
        assertTrue(result.isEmpty());
    }
}