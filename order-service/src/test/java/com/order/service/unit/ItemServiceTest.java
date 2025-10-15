package com.order.service.unit;

import com.order.service.model.Item;
import com.order.service.model.dto.ItemDto;
import com.order.service.repository.ItemRepository;
import com.order.service.service.ItemServiceImpl;
import com.order.service.service.mapper.ItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    private ItemServiceImpl itemService;

    private Item testItem;
    private ItemDto testItemDto;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, itemMapper);

        testItem = new Item();
        testItem.setItemId(1L);
        testItem.setName("Test Item");
        testItem.setPrice("99.99");

        testItemDto = new ItemDto();
        testItemDto.setItemId(1L);
        testItemDto.setName("Test Item");
        testItemDto.setPrice("99.99");
    }

    @Test
    void createItem_ShouldReturnItemId_WhenValidInput() {
        
        when(itemMapper.toEntity(testItemDto)).thenReturn(testItem);
        when(itemRepository.save(testItem)).thenReturn(testItem);

        
        Long result = itemService.createItem(testItemDto);

        
        assertEquals(1L, result);
        verify(itemRepository).save(testItem);
        verify(itemMapper).toEntity(testItemDto);
    }

    @Test
    void findItemById_ShouldReturnItemDto_WhenItemExists() {
        
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemMapper.toDto(testItem)).thenReturn(testItemDto);

        
        ItemDto result = itemService.findItemById(1L);

        
        assertNotNull(result);
        assertEquals(1L, result.getItemId());
        assertEquals("Test Item", result.getName());
        verify(itemRepository).findById(1L);
        verify(itemMapper).toDto(testItem);
    }

    @Test
    void findItemById_ShouldThrowException_WhenItemNotFound() {
        
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        
        assertThrows(NoSuchElementException.class, () -> itemService.findItemById(999L));
        verify(itemRepository).findById(999L);
        verify(itemMapper, never()).toDto(any());
    }

    @Test
    void findItemsByIdCsv_ShouldReturnItems_WhenIdsExist() {
        
        String idCsv = "1,2,3";
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        Item item2 = new Item();
        item2.setItemId(2L);
        Item item3 = new Item();
        item3.setItemId(3L);

        when(itemRepository.findAllById(ids)).thenReturn(Arrays.asList(testItem, item2, item3));

        
        List<Item> result = itemService.findItemsByIdCsv(idCsv);

        
        assertEquals(3, result.size());
        verify(itemRepository).findAllById(ids);
    }

    @Test
    void findItemsByIdCsv_ShouldReturnEmptyList_WhenNoItemsFound() {
        
        String idCsv = "1,2,3";
        when(itemRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        
        List<Item> result = itemService.findItemsByIdCsv(idCsv);

        
        assertTrue(result.isEmpty());
        verify(itemRepository).findAllById(Arrays.asList(1L, 2L, 3L));
    }

    @Test
    void updateItem_ShouldUpdateAndReturnId_WhenItemExists() {
        
        ItemDto updatedDto = new ItemDto();
        updatedDto.setName("Updated Item");
        updatedDto.setPrice("149.99");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemRepository.save(testItem)).thenReturn(testItem);

        
        Long result = itemService.updateItem(1L, updatedDto);

        
        assertEquals(1L, result);
        assertEquals("Updated Item", testItem.getName());
        assertEquals("149.99", testItem.getPrice());
        verify(itemRepository).findById(1L);
        verify(itemRepository).save(testItem);
    }

    @Test
    void updateItem_ShouldNotChangeFields_WhenSameValuesProvided() {
        
        ItemDto updatedDto = new ItemDto();
        updatedDto.setName("Test Item");  // Same as original
        updatedDto.setPrice("99.99");     // Same as original

        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemRepository.save(testItem)).thenReturn(testItem);

        
        Long result = itemService.updateItem(1L, updatedDto);

        
        assertEquals(1L, result);
        assertEquals("Test Item", testItem.getName());  // Should remain unchanged
        assertEquals("99.99", testItem.getPrice());     // Should remain unchanged
        verify(itemRepository).save(testItem);
    }

    @Test
    void updateItem_ShouldThrowException_WhenItemNotFound() {
        
        ItemDto updatedDto = new ItemDto();
        updatedDto.setName("Updated Item");
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        
        assertThrows(NoSuchElementException.class, () -> itemService.updateItem(999L, updatedDto));
        verify(itemRepository).findById(999L);
        verify(itemRepository, never()).save(any());
    }

    @Test
    void deleteItem_ShouldReturnId_WhenItemDeleted() {
        
        doNothing().when(itemRepository).deleteById(1L);

        
        Long result = itemService.deleteItem(1L);

        
        assertEquals(1L, result);
        verify(itemRepository).deleteById(1L);
    }

    @Test
    void deleteItem_ShouldReturnId_WhenItemNotExists() {
        
        doNothing().when(itemRepository).deleteById(999L);

        
        Long result = itemService.deleteItem(999L);

        
        assertEquals(999L, result);
        verify(itemRepository).deleteById(999L);
    }


    @Test
    void findItemsByIdCsv_ShouldHandleSingleId() {
        
        String idCsv = "1";
        when(itemRepository.findAllById(Arrays.asList(1L))).thenReturn(Collections.singletonList(testItem));

        
        List<Item> result = itemService.findItemsByIdCsv(idCsv);

        
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getItemId());
        verify(itemRepository).findAllById(Arrays.asList(1L));
    }

    @Test
    void findItemsByIdCsv_ShouldThrowException_WhenInvalidIdFormat() {
        
        String idCsv = "1,abc,3";

        
        assertThrows(NumberFormatException.class, () -> itemService.findItemsByIdCsv(idCsv));
        verify(itemRepository, never()).findAllById(any());
    }
}