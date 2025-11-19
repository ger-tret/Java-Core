package com.payment.service.repository;

import com.payment.service.model.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByStatus (String status);
    Payment findByOrderId(Long orderId);
    Payment findByUserId(Long userId);

    @Query("SELECT SUM(P.payment_amount) FROM P payments WHERE P.timestamp > ?1 OR P.timestamp < ?2")
    Integer getSumByPeriod(Date periodStart, Date periodEnd);

    @Transactional
    @Modifying
    @Query("update Payment set status = ?2 where id = ?1")
    Long updateStatus(Long id, String status);
}
