package com.order.service.model.dto;

import com.order.service.model.enums.OrderStatus;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDto {
    @Id
    private Long orderId;
    private Long userId;
    private OrderStatus orderStatus;
    private Date creationDate;
    private List<ItemDto> orderItems;

    public OrderDto(Long userId, List<ItemDto> items){
        this.creationDate = new Date();
        this.orderStatus = OrderStatus.PENDING;
        this.userId = userId;
        this.orderItems = items;
    }

}
