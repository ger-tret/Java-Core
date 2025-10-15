package com.order.service.service.mapper;

import com.order.service.model.Order;
import com.order.service.model.dto.CreateOrderRequestDto;
import com.order.service.model.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    OrderDto toDto(CreateOrderRequestDto createOrderRequestDto);

    List<OrderDto> orderToDTOList(List<Order> orders);
}