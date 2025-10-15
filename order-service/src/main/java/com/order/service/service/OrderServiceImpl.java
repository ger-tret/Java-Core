package com.order.service.service;


import com.order.service.model.Order;
import com.order.service.model.dto.IdDto;
import com.order.service.model.dto.OrderDto;
import com.order.service.model.enums.OrderStatus;
import com.order.service.repository.OrderRepository;
import com.order.service.service.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Long createOrder(OrderDto orderDto) {
        return new IdDto(orderRepository.save(orderMapper.toEntity(orderDto)).getOrderId()).id();
    }

    @Override
    public OrderDto findOrderById(Long id) {
        return orderMapper.toDto(orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Order by ID= " + id + " not found")));
    }

    @Override
    public List<Order> findOrdersByIdCsv(String idCsv) {
        List<Long> ids = Arrays.stream(idCsv.split(",")).map(Long::parseLong).toList();
        List<Order> foundOrders = orderRepository.findAllById(ids).stream().toList();
        return foundOrders;
    }

    @Override
    public List<Order> findOrdersByStatus(String status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @Override
    public Long updateOrderStatus(Long id, OrderStatus orderStatus){
        orderRepository.setOrderStatusById(id, orderStatus);
        return id;
    }

    @Override
    @Transactional
    public Long updateOrder(Long id, OrderDto updatedOrder) {
        return orderRepository.findById(id).map(order -> {
           order.setOrderStatus(updatedOrder.getOrderStatus() == order.getOrderStatus() ? order.getOrderStatus() : updatedOrder.getOrderStatus());
           order.setCreationDate(updatedOrder.getCreationDate() == order.getCreationDate() ? order.getCreationDate() : updatedOrder.getCreationDate());
            return orderRepository.save(order);
        }).orElseThrow(() -> new NoSuchElementException("Item for ID= " + id + " not found")).getOrderId();
    }

    @Override
    @Transactional
    public Long deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return id;
    }
}
