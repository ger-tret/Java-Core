package com.test.task.trainee.stream.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;
}

