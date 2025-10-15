package com.order.service.service;

import com.order.service.model.Item;
import com.order.service.model.dto.IdDto;
import com.order.service.model.dto.ItemDto;
import com.order.service.repository.ItemRepository;
import com.order.service.service.mapper.ItemMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public Long createItem(ItemDto itemDto) {
        return new IdDto(itemRepository.save(itemMapper.toEntity(itemDto)).getItemId()).id();
    }

    @Override
    public ItemDto findItemById(Long id) {
        return itemMapper.toDto(itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Item for ID= " + id + " not found")));
    }

    @Override
    public List<Item> findItemsByIdCsv(String idCsv) {
        List<Long> ids = Arrays.stream(idCsv.split(",")).map(Long::parseLong).toList();
        List<Item> foundItems = itemRepository.findAllById(ids).stream().toList();
        return foundItems;
    }

    @Override
    @Transactional
    public Long updateItem(Long id, ItemDto updatedItem) {
        return itemRepository.findById(id).map(item -> {
            item.setName(updatedItem.getName() == item.getName() ? item.getName() : updatedItem.getName());
            item.setPrice(updatedItem.getPrice() == item.getPrice() ? item.getPrice() : updatedItem.getPrice());
            return itemRepository.save(item);
        }).orElseThrow(() -> new NoSuchElementException("Item for ID= " + id + " not found")).getItemId();
    }

    @Override
    public Long deleteItem(Long id) {
        itemRepository.deleteById(id);
        return id;
    }
}
