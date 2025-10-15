package com.order.service.service.mapper;

import com.order.service.model.Item;
import com.order.service.model.dto.ItemDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toDto(Item item);
    Item toEntity(ItemDto itemDto);
    List<ItemDto> itemToDTOList(List<Item> orderItems);
}