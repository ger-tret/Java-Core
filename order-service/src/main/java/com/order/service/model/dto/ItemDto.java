package com.order.service.model.dto;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class ItemDto {
    @Id
    private Long itemId;
    private String name;
    private String price;
}
