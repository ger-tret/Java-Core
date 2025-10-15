package com.order.service.service;

import com.order.service.model.Order;
import com.order.service.model.dto.OrderDto;
import com.order.service.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderDto orderDto);
    OrderDto findOrderById(Long id);
    List<Order> findOrdersByIdCsv(String idCsv);
    List<Order> findOrdersByStatus(String status);
    List<Order> getAllOrders();
    Long updateOrder(Long id, OrderDto updatedOrder);
    Long updateOrderStatus(Long id, OrderStatus orderStatus);
    Long deleteOrder(Long id);

}
