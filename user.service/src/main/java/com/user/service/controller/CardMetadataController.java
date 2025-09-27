package com.user.service.controller;


import com.user.service.model.dto.CardMetadataDto;
import com.user.service.model.entity.CardMetadata;
import com.user.service.service.CardServiceImpl;
import com.user.service.validation.annotation.IdCsvValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
@Validated
public class CardMetadataController {

    private CardServiceImpl service;

    @Autowired
    CardMetadataController(CardServiceImpl cardService){
        this.service = cardService;
    }

    @PostMapping
    ResponseEntity<Long> createCard(@RequestBody @Valid CardMetadataDto cardDto){
        return ResponseEntity.ok((service.createCard(cardDto)));
    }

    @PostMapping("/update")
    ResponseEntity<Long> updateCard(@RequestParam("id") Long id, @RequestBody @Valid CardMetadataDto cardDto){
        return ResponseEntity.ok((service.updateCardMetadata(id, cardDto)));
    }

    @GetMapping
    ResponseEntity<CardMetadataDto> findCard(@RequestParam("id") Long id){
        return ResponseEntity.ok(service.findCardMetadataById(id));
    }

    @GetMapping("/findbycsv")
    ResponseEntity<List<CardMetadata>> findCardsByIdCsv(@RequestParam("id") @IdCsvValidation String id){
        return ResponseEntity.ok(((service.findCardsMetadatasByIdCsv(id))));
    }

    @GetMapping("/findbyusemail")
    ResponseEntity<List<CardMetadata>> findCardsByUserEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(service.getCardsByUserEmail(email));
    }

    @DeleteMapping
    ResponseEntity<Long> deleteCard(@RequestParam("id") Long id){
        return ResponseEntity.ok(service.deleteCardMetadata(id));
    }

}
