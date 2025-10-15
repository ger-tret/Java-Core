package com.order.service.unit;


import com.order.service.client.UserServiceClient;
import com.order.service.model.Order;
import com.order.service.model.dto.ItemDto;
import com.order.service.model.dto.OrderDto;
import com.order.service.model.enums.OrderStatus;
import com.order.service.repository.OrderRepository;
import com.order.service.service.OrderServiceImpl;
import com.order.service.service.mapper.OrderMapper;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserServiceClient userServiceClient;

    private OrderServiceImpl orderService;

    private Order testOrder;
    private OrderDto testOrderDto;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, orderMapper);

        testOrder = new Order();
        testOrder.setOrderId(1L);
        testOrder.setUserId(1L);
        testOrder.setOrderStatus(OrderStatus.PENDING);
        testOrder.setCreationDate(new Date());
        testOrder.setOrderItems(new ArrayList<>());

        testOrderDto = new OrderDto(1L, List.of(new ItemDto()));
        testOrderDto.setOrderId(1L);
        testOrderDto.setOrderStatus(OrderStatus.PENDING);
        testOrderDto.setCreationDate(new Date());
    }

    @Test
    void createOrder_ShouldReturnOrderId_WhenValidInput() {
        when(orderMapper.toEntity(testOrderDto)).thenReturn(testOrder);
        when(orderRepository.save(testOrder)).thenReturn(testOrder);

        
        Long result = orderService.createOrder(testOrderDto);

        
        assertEquals(1L, result);
        verify(orderRepository).save(testOrder);
        verify(orderMapper).toEntity(testOrderDto);
    }

    @Test
    void findOrderById_ShouldReturnOrderDto_WhenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderMapper.toDto(testOrder)).thenReturn(testOrderDto);

        
        OrderDto result = orderService.findOrderById(1L);

        
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        verify(orderRepository).findById(1L);
        verify(orderMapper).toDto(testOrder);
    }

    @Test
    void findOrderById_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.findOrderById(999L));
        verify(orderRepository).findById(999L);
    }

    @Test
    void findOrdersByIdCsv_ShouldReturnOrders_WhenIdsExist() {
        String idCsv = "1,2,3";
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        Order order2 = new Order();
        order2.setOrderId(2L);
        Order order3 = new Order();
        order3.setOrderId(3L);

        when(orderRepository.findAllById(ids)).thenReturn(Arrays.asList(testOrder, order2, order3));

        
        List<Order> result = orderService.findOrdersByIdCsv(idCsv);

        
        assertEquals(3, result.size());
        verify(orderRepository).findAllById(ids);
    }

    @Test
    void findOrdersByIdCsv_ShouldReturnEmptyList_WhenNoOrdersFound() {
        String idCsv = "1,2,3";
        when(orderRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        
        List<Order> result = orderService.findOrdersByIdCsv(idCsv);

        
        assertTrue(result.isEmpty());
        verify(orderRepository).findAllById(Arrays.asList(1L, 2L, 3L));
    }

    @Test
    void findOrdersByStatus_ShouldReturnOrders_WhenStatusExists() {
        
        when(orderRepository.findAllByStatus("PENDING")).thenReturn(Arrays.asList(testOrder));

        
        List<Order> result = orderService.findOrdersByStatus("PENDING");

        
        assertEquals(1, result.size());
        assertEquals(OrderStatus.PENDING, result.get(0).getOrderStatus());
        verify(orderRepository).findAllByStatus("PENDING");
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        
        Order order2 = new Order();
        order2.setOrderId(2L);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(testOrder, order2));

        
        List<Order> result = orderService.getAllOrders();

        
        assertEquals(2, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus_WhenOrderExists() {
        doNothing().when(orderRepository).setOrderStatusById(1L, OrderStatus.COMPLETED);

        
        Long result = orderService.updateOrderStatus(1L, OrderStatus.COMPLETED);

        
        assertEquals(1L, result);
        verify(orderRepository).setOrderStatusById(1L, OrderStatus.COMPLETED);
    }

    @Test
    void updateOrder_ShouldUpdateAndReturnId_WhenOrderExists() {
        OrderDto updatedDto = new OrderDto(1L, List.of(new ItemDto()));
        updatedDto.setOrderStatus(OrderStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(testOrder)).thenReturn(testOrder);

        
        Long result = orderService.updateOrder(1L, updatedDto);

        
        assertEquals(1L, result);
        assertEquals(OrderStatus.COMPLETED, testOrder.getOrderStatus());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(testOrder);
    }

    @Test
    void updateOrder_ShouldThrowException_WhenOrderNotFound() {
        OrderDto updatedDto = new OrderDto(999L, List.of(new ItemDto()));
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.updateOrder(999L, updatedDto));
        verify(orderRepository).findById(999L);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void deleteOrder_ShouldReturnId_WhenOrderDeleted() {
        
        doNothing().when(orderRepository).deleteById(1L);

        
        Long result = orderService.deleteOrder(1L);

        
        assertEquals(1L, result);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void findOrdersByIdCsv_ShouldThrowException_WhenInvalidIdFormat() {
        
        String idCsv = "1,invalid,3";

        assertThrows(NumberFormatException.class, () -> orderService.findOrdersByIdCsv(idCsv));
        verify(orderRepository, never()).findAllById(any());
    }
}