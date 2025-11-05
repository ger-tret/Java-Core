package com.order.service.controller;

import com.order.service.client.UserServiceClient;
import com.order.service.model.Order;
import com.order.service.model.dto.CreateOrderRequestDto;
import com.order.service.model.dto.OrderDto;
import com.order.service.model.enums.OrderStatus;
import com.order.service.service.OrderService;
import com.order.service.service.mapper.OrderMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserServiceClient userServiceClient;
    private final OrderMapper mapper;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequestDto createOrderRequest) {
       Long id = orderService.createOrder(createOrderRequest);
       return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @GetMapping("/user/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable("status") String status) {
        return ResponseEntity.ok(orderService.findOrdersByStatus(status));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(mapper.orderToDTOList(orderService.getAllOrders()));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Long> updateOrderStatus(@PathVariable("id") Long id, @RequestParam String status) {
        return ResponseEntity.ok((orderService.updateOrderStatus(id, OrderStatus.valueOf(status))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteOrder(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }
}