package com.order.service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.order.service.model.enums.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OrderValidationDetailsDto {
    private long orderId;
    private long userId;
    private OrderStatus orderStatus;
    private Date creationDate;
    private List<ItemDto> orderItems;
}