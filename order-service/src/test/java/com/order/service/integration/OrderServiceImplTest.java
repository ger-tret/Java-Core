package com.order.service.integration;

import com.order.service.model.Item;
import com.order.service.model.dto.ItemDto;
import com.order.service.repository.ItemRepository;
import com.order.service.service.ItemService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
class OrderServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine").withDatabaseName("testdatabase").withUsername("test").withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create item successfully when valid request")
    void createItem_ShouldCreateItemSuccessfully_WhenValidRequest() {

        ItemDto itemDto = createValidItemDto();


        Long itemId = itemService.createItem(itemDto);


        assertNotNull(itemId);

        ItemDto result = itemService.findItemById(itemId);
        assertEquals("Test Item", result.getName());
        assertEquals("99.99", result.getPrice());
        assertEquals(1, itemRepository.count());
    }

    @Test
    @DisplayName("Should return item when item exists")
    void findItemById_ShouldReturnItem_WhenItemExists() {

        Long itemId = itemService.createItem(createValidItemDto());


        ItemDto result = itemService.findItemById(itemId);


        assertNotNull(result);
        assertEquals(itemId, result.getItemId());
        assertEquals("Test Item", result.getName());
        assertEquals("99.99", result.getPrice());
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when item does not exist")
    void findItemById_ShouldThrowNoSuchElementException_WhenItemDoesNotExist() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> itemService.findItemById(999L));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Should return items when items exist for CSV")
    void findItemsByIdCsv_ShouldReturnItems_WhenItemsExist() {

        Long itemId1 = itemService.createItem(createValidItemDto());
        Long itemId2 = itemService.createItem(createValidItemDto("Item 2", "49.99"));

        String idCsv = itemId1 + "," + itemId2;


        List<Item> result = itemService.findItemsByIdCsv(idCsv);


        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(item -> itemId1.equals(item.getItemId())));
        assertTrue(result.stream().anyMatch(item -> itemId2.equals(item.getItemId())));
    }

    @Test
    @DisplayName("Should return empty list when no items found for CSV")
    void findItemsByIdCsv_ShouldReturnEmptyList_WhenNoItemsFound() {

        List<Item> result = itemService.findItemsByIdCsv("999,1000");


        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should update item when item exists")
    void updateItem_ShouldUpdateItem_WhenItemExists() {

        Long itemId = itemService.createItem(createValidItemDto());

        ItemDto updatedItemDto = new ItemDto();
        updatedItemDto.setName("Updated Item");
        updatedItemDto.setPrice("149.99");


        Long updatedItemId = itemService.updateItem(itemId, updatedItemDto);


        assertEquals(itemId, updatedItemId);

        ItemDto result = itemService.findItemById(itemId);
        assertEquals("Updated Item", result.getName());
        assertEquals("149.99", result.getPrice());
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when item does not exist for update")
    void updateItem_ShouldThrowNoSuchElementException_WhenItemDoesNotExist() {

        ItemDto itemDto = createValidItemDto();

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> itemService.updateItem(999L, itemDto));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Should delete item when item exists")
    void deleteItem_ShouldDeleteItem_WhenItemExists() {

        Long itemId = itemService.createItem(createValidItemDto());
        assertTrue(itemRepository.existsById(itemId));


        Long deletedItemId = itemService.deleteItem(itemId);


        assertEquals(itemId, deletedItemId);
        assertFalse(itemRepository.existsById(itemId));

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> itemService.findItemById(itemId));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Should return ID when deleting non-existent item")
    void deleteItem_ShouldReturnId_WhenItemDoesNotExist() {

        Long result = itemService.deleteItem(999L);


        assertEquals(999L, result);
    }

    private ItemDto createValidItemDto() {
        return createValidItemDto("Test Item", "99.99");
    }

    private ItemDto createValidItemDto(String name, String price) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name);
        itemDto.setPrice(price);
        return itemDto;
    }
}