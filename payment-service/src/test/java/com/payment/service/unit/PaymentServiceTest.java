package com.payment.service.unit;

import com.payment.service.client.NumberGeneretionReceiveClient;
import com.payment.service.model.Payment;
import com.payment.service.model.PaymentStatus;
import com.payment.service.model.dto.CreatePaymentDto;
import com.payment.service.model.dto.PaymentDto;
import com.payment.service.repository.PaymentRepository;
import com.payment.service.service.PaymentServiceImpl;
import com.payment.service.service.mapper.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private NumberGeneretionReceiveClient numberGeneretionReceiveClient;

    @Mock
    private PaymentMapper paymentMapper;

    private PaymentServiceImpl paymentService;

    private Payment testPayment;
    private PaymentDto testPaymentDto;
    private CreatePaymentDto testCreatePaymentDto;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(paymentRepository, numberGeneretionReceiveClient, paymentMapper);

        testPayment = new Payment();
        testPayment.setPaymentId(1L);
        testPayment.setOrderId(100L);
        testPayment.setUserId(200L);
        testPayment.setStatus(PaymentStatus.SUCCESS);
        testPayment.setTimestamp(new Date());
        testPayment.setPaymentAmount(BigDecimal.valueOf(99.99));

        testPaymentDto = new PaymentDto();
        testPaymentDto.setPaymentId(1L);
        testPaymentDto.setOrderId(100L);
        testPaymentDto.setUserId(200L);
        testPaymentDto.setStatus(String.valueOf(PaymentStatus.SUCCESS));
        testPaymentDto.setTimestamp(new Date());
        testPaymentDto.setPaymentAmount(BigDecimal.valueOf(99.99));

        testCreatePaymentDto = new CreatePaymentDto();
        testCreatePaymentDto.setOrderId(100L);
        testCreatePaymentDto.setUserId(200L);
        testCreatePaymentDto.setPaymentAmount(BigDecimal.valueOf(99.99));
    }

    @Test
    void createPayment_ShouldReturnPaymentWithSuccessStatus_WhenRandomNumberIsEven() {
        
        when(paymentMapper.toEntity(testCreatePaymentDto)).thenReturn(testPayment);
        when(numberGeneretionReceiveClient.getPaymentStatus()).thenReturn(200);
        when(paymentRepository.save(testPayment)).thenReturn(testPayment);

        
        Payment result = paymentService.createPayment(testCreatePaymentDto);

        
        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        verify(paymentMapper).toEntity(testCreatePaymentDto);
        verify(numberGeneretionReceiveClient).getPaymentStatus();
        verify(paymentRepository).save(testPayment);
    }

    @Test
    void createPayment_ShouldReturnPaymentWithFailedStatus_WhenRandomNumberIsOdd() {
        
        when(paymentMapper.toEntity(testCreatePaymentDto)).thenReturn(testPayment);
        when(numberGeneretionReceiveClient.getPaymentStatus()).thenReturn(201);
        when(paymentRepository.save(testPayment)).thenReturn(testPayment);

        
        Payment result = paymentService.createPayment(testCreatePaymentDto);

        
        assertNotNull(result);
        assertEquals(PaymentStatus.FAILED, result.getStatus());
        verify(paymentMapper).toEntity(testCreatePaymentDto);
        verify(numberGeneretionReceiveClient).getPaymentStatus();
        verify(paymentRepository).save(testPayment);
    }

    @Test
    void getPaymentByPaymentId_ShouldReturnPaymentDto_WhenPaymentExists() {
        
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(paymentMapper.toDto(testPayment)).thenReturn(testPaymentDto);

        
        PaymentDto result = paymentService.getPaymentByPaymentId(1L);

        
        assertNotNull(result);
        assertEquals(1L, result.getPaymentId());
        assertEquals(100L, result.getOrderId());
        assertEquals(200L, result.getUserId());
        verify(paymentRepository).findById(1L);
        verify(paymentMapper).toDto(testPayment);
    }

    @Test
    void getPaymentByPaymentId_ShouldThrowException_WhenPaymentNotFound() {
        
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> paymentService.getPaymentByPaymentId(999L));
        verify(paymentRepository).findById(999L);
        verify(paymentMapper, never()).toDto(any());
    }

    @Test
    void getPaymentByOrderId_ShouldReturnPaymentDto_WhenPaymentExists() {
        
        when(paymentRepository.findByOrderId(100L)).thenReturn(testPayment);
        when(paymentMapper.toDto(testPayment)).thenReturn(testPaymentDto);

        
        PaymentDto result = paymentService.getPaymentByOrderId(100L);

        
        assertNotNull(result);
        assertEquals(100L, result.getOrderId());
        verify(paymentRepository).findByOrderId(100L);
        verify(paymentMapper).toDto(testPayment);
    }

    @Test
    void getPaymentByUserId_ShouldReturnPaymentDto_WhenPaymentExists() {
        
        when(paymentRepository.findByUserId(200L)).thenReturn(testPayment);
        when(paymentMapper.toDto(testPayment)).thenReturn(testPaymentDto);

        
        PaymentDto result = paymentService.getPaymentByUserId(200L);

        
        assertNotNull(result);
        assertEquals(200L, result.getUserId());
        verify(paymentRepository).findByUserId(200L);
        verify(paymentMapper).toDto(testPayment);
    }

    @Test
    void getPaymentByStatus_ShouldReturnPaymentDtos_WhenPaymentsExist() {
        
        List<Payment> paymentList = Arrays.asList(testPayment);
        List<PaymentDto> paymentDtoList = Arrays.asList(testPaymentDto);

        when(paymentRepository.findAllByStatus("SUCCESS")).thenReturn(paymentList);
        when(paymentMapper.toDtos(paymentList)).thenReturn(paymentDtoList);

        
        List<PaymentDto> result = paymentService.getPaymentByStatus("SUCCESS");

        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SUCCESS", result.getFirst().getStatus());
        verify(paymentRepository).findAllByStatus("SUCCESS");
        verify(paymentMapper).toDtos(paymentList);
    }

    @Test
    void getPaymentByStatus_ShouldReturnEmptyList_WhenNoPaymentsWithStatus() {
        
        when(paymentRepository.findAllByStatus("FAILED")).thenReturn(Collections.emptyList());
        when(paymentMapper.toDtos(Collections.emptyList())).thenReturn(Collections.emptyList());

        
        List<PaymentDto> result = paymentService.getPaymentByStatus("FAILED");

        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentRepository).findAllByStatus("FAILED");
        verify(paymentMapper).toDtos(Collections.emptyList());
    }

    @Test
    void getPaymentSumByDatePeriod_ShouldReturnSum_WhenValidPeriodProvided() {
        
        Date periodStart = new Date(System.currentTimeMillis() - 86400000); // 1 day ago
        Date periodEnd = new Date();
        Integer expectedSum = 500;

        when(paymentRepository.getSumByPeriod(periodStart, periodEnd)).thenReturn(expectedSum);

        
        Integer result = paymentService.getPaymentSumByDatePeriod(periodStart, periodEnd);

        
        assertNotNull(result);
        assertEquals(expectedSum, result);
        verify(paymentRepository).getSumByPeriod(periodStart, periodEnd);
    }

    @Test
    void getPaymentSumByDatePeriod_ShouldReturnZero_WhenNoPaymentsInPeriod() {
        
        Date periodStart = new Date(System.currentTimeMillis() - 86400000);
        Date periodEnd = new Date();

        when(paymentRepository.getSumByPeriod(periodStart, periodEnd)).thenReturn(0);

        
        Integer result = paymentService.getPaymentSumByDatePeriod(periodStart, periodEnd);

        
        assertNotNull(result);
        assertEquals(0, result);
        verify(paymentRepository).getSumByPeriod(periodStart, periodEnd);
    }

    @Test
    void deletePayment_ShouldReturnId_WhenPaymentDeleted() {
        
        doNothing().when(paymentRepository).deleteById(1L);

        
        Long result = paymentService.deletePayment(1L);

        
        assertEquals(1L, result);
        verify(paymentRepository).deleteById(1L);
    }

    @Test
    void deletePayment_ShouldReturnId_WhenPaymentNotExists() {
        
        doNothing().when(paymentRepository).deleteById(999L);

        
        Long result = paymentService.deletePayment(999L);

        
        assertEquals(999L, result);
        verify(paymentRepository).deleteById(999L);
    }

    @Test
    void updatePaymentStatus_ShouldReturnId_WhenStatusUpdated() {
        
        Long paymentId = 1L;
        String newStatus = "FAILED";
        when(paymentRepository.updateStatus(paymentId, newStatus)).thenReturn(paymentId);

        
        Long result = paymentService.updatePaymentStatus(paymentId, newStatus);

        
        assertEquals(paymentId, result);
        verify(paymentRepository).updateStatus(paymentId, newStatus);
    }

    @Test
    void getAllPayments_ShouldReturnAllPaymentDtos_WhenPaymentsExist() {
        
        List<Payment> paymentList = Arrays.asList(testPayment);
        List<PaymentDto> paymentDtoList = Arrays.asList(testPaymentDto);

        when(paymentRepository.findAll()).thenReturn(paymentList);
        when(paymentMapper.toDtos(paymentList)).thenReturn(paymentDtoList);

        
        List<PaymentDto> result = paymentService.getAllPayments();

        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentRepository).findAll();
        verify(paymentMapper).toDtos(paymentList);
    }

    @Test
    void getAllPayments_ShouldReturnEmptyList_WhenNoPayments() {
        
        when(paymentRepository.findAll()).thenReturn(Collections.emptyList());
        when(paymentMapper.toDtos(Collections.emptyList())).thenReturn(Collections.emptyList());

        
        List<PaymentDto> result = paymentService.getAllPayments();

        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentRepository).findAll();
        verify(paymentMapper).toDtos(Collections.emptyList());
    }

    @Test
    void createPayment_ShouldSetTimestamp_WhenCreatingPayment() {
        
        Payment paymentWithoutTimestamp = new Payment();
        paymentWithoutTimestamp.setOrderId(100L);
        paymentWithoutTimestamp.setUserId(200L);
        paymentWithoutTimestamp.setPaymentAmount(BigDecimal.valueOf(99.99));

        when(paymentMapper.toEntity(testCreatePaymentDto)).thenReturn(paymentWithoutTimestamp);
        when(numberGeneretionReceiveClient.getPaymentStatus()).thenReturn(200);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment savedPayment = invocation.getArgument(0);
            savedPayment.setPaymentId(1L);
            return savedPayment;
        });

        
        Payment result = paymentService.createPayment(testCreatePaymentDto);

        
        assertNotNull(result);
        assertNotNull(result.getTimestamp());
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        verify(paymentRepository).save(any(Payment.class));
    }
}