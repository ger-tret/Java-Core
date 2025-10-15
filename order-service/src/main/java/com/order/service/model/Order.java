package com.order.service.model;

import com.order.service.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderId;

    private Long userId;

    private OrderStatus orderStatus;

    private Date creationDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> orderItems;


}
