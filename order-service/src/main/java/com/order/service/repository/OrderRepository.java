package com.order.service.repository;

import com.order.service.model.Order;
import com.order.service.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus (String status);

    @Modifying
    @Query("update Order o set o.order_status = ?2 where o.id = ?1")
    void setOrderStatusById(Long id, OrderStatus orderStatus);
}
