package com.order.service.model.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateOrderRequestDto {

    @NotNull(message = "User id is required")
    private Long userId;

    @Valid
    @Size(min = 1, message = "Order must contain at least one item")
    private List<ItemDto> items;
}