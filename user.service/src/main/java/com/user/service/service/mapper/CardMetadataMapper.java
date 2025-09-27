package com.user.service.service.mapper;

import com.user.service.model.dto.CardMetadataDto;
import com.user.service.model.entity.CardMetadata;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CardMetadataMapper {
    CardMetadataDto toDto(CardMetadata cardMetadata);
    CardMetadata toEntity(CardMetadataDto cardMetadataDto);
    List<CardMetadataDto> toDto(List<CardMetadata> cardMetadata);
    List<CardMetadata> toEntity(List<CardMetadataDto> cardMetadataDto);
}