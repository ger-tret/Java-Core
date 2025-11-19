package com.payment.service.client;


import com.payment.service.model.Payment;
import com.payment.service.model.dto.CreatePaymentDto;
import com.payment.service.model.dto.PaymentDto;
import com.payment.service.service.PaymentService;
import com.payment.service.service.mapper.PaymentMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper mapper;


    @PostMapping
    public ResponseEntity<Long> createOrder(@Valid @RequestBody CreatePaymentDto createPaymentDto) {
        return ResponseEntity.ok(paymentService.createPayment(createPaymentDto).getPaymentId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentService.getPaymentByPaymentId(id));
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<PaymentDto>> getPaymentByStatus(@PathVariable("status") String status) {
        return ResponseEntity.ok(paymentService.getPaymentByStatus(status));
    }

    @GetMapping
    public Integer getSumPaymentsByPeriod(@RequestParam Date periodStart, @RequestParam Date periodEnd){
        return ResponseEntity.ok(paymentService.getPaymentSumByDatePeriod(periodStart,periodEnd)).getBody();
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Long> updatePaymentStatus(@PathVariable("id") Long id, @RequestParam String status) {
        return ResponseEntity.ok((paymentService.updatePaymentStatus(id, status)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deletePayment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentService.deletePayment(id));
    }

}