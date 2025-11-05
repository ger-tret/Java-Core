package com.order.service.model.dto;

import lombok.Data;

@Data
public class ItemDto {
    private Long itemId;
    private String name;
    private String price;
}
