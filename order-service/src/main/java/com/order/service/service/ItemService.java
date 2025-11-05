package com.order.service.service;

import com.order.service.model.Item;
import com.order.service.model.dto.ItemDto;

import java.util.List;

public interface ItemService {
    Long createItem(ItemDto itemDto);
    ItemDto findItemById(Long id);
    List<Item> findItemsByIdCsv(String idCsv);
    Long updateItem(Long id, ItemDto updatedItem);
    Long deleteItem(Long id);

}
