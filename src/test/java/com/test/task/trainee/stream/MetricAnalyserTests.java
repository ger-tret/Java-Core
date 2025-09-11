package com.test.task.trainee.stream;

import com.test.task.trainee.stream.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@MockitoBean(types = {MetricAnalyser.class, Customer.class, Order.class, OrderItem.class})
public class MetricAnalyserTests {

    private MetricAnalyser metricAnalyser;
    @Autowired
    void MetricAnalyserTests(MetricAnalyser metricAnalyser){
        this.metricAnalyser = metricAnalyser;
    }
    private List<Order> orderList;

    @BeforeEach
    void setUp(){
        metricAnalyser = new MetricAnalyser();
        orderList = new ArrayList<>();
        Customer customer1 = new Customer("1", "John", "john@email.com", LocalDateTime.now(), 20, "New York");
        Customer customer2 = new Customer("2", "Alice", "alice@email.com", LocalDateTime.now(), 24, "New York"); // Null city
        Customer customer3 = new Customer("3", "Aaron", "aaron@email.com", LocalDateTime.now(), 26, "Warsaw");
        Customer customer4 = new Customer("4", "Bob", "bob@email.com", LocalDateTime.now(), 19, "Minsk");
        OrderItem item1 = new OrderItem("Laptop", 1, 1200.0, Category.ELECTRONICS);
        OrderItem item2 = new OrderItem("T-Shirt", 2, 25.0, Category.CLOTHING);
        OrderItem item3 = new OrderItem("Book", 1, 15.0, Category.BOOKS);
        OrderItem item4 = new OrderItem("Smartphone", 1, 800.0, Category.ELECTRONICS);
        Order order1 = new Order("ORD001", LocalDateTime.now().minusDays(5),
                customer1, List.of(item1, item2), OrderStatus.DELIVERED); //1225
        Order order2 = new Order("ORD002", LocalDateTime.now().minusDays(3),
                customer2, List.of(item3), OrderStatus.DELIVERED); //15
        Order order3 = new Order("ORD003", LocalDateTime.now().minusDays(2),
                customer3, List.of(item4, item2, item3), OrderStatus.PROCESSING);
        Order order4 = new Order("ORD004", LocalDateTime.now().minusDays(1),
                customer1, List.of(item3, item1), OrderStatus.DELIVERED); //1215
        Order order5 = new Order("ORD005", LocalDateTime.now(),
                customer4, List.of(item2), OrderStatus.CANCELLED);
        orderList.addAll(List.of(order1, order2, order3, order4, order5));
    }

    @Test
    void uniqueCitiesAnalysis_test(){
        List<String> result = metricAnalyser.uniqueCitiesAnalysis(orderList);

        assertEquals(3, result.size());
        assertTrue(result.contains("Minsk"));
        assertTrue(result.contains("Warsaw"));
        assertTrue(result.contains("New York"));
        assertFalse(result.contains(null));

    }

    @Test
    void totalIncomeAnalysis_test(){
        double result = metricAnalyser.totalIncomeAnalysis(orderList);
        double expected = 3320;
        assertEquals(expected, result);
    }

    @Test
    void mostPopularItemBySalesAnalysis_test(){
        String result = metricAnalyser.mostPopularProductBySalesAnalysis(orderList);
        assertEquals("T-Shirt", result);
    }

    @Test
    void AverageCheckForDeliveredAnalysis_test(){
        double result = metricAnalyser.averageCheckAmountForDeliveredAnalysis(orderList);
        double expected = 491.0;
        assertEquals(expected, result);
    }

    @Test
    void testCustomersWithHighestOrderCountAnalysis(){
        List<Customer> result = metricAnalyser.customersWithHighOrdersCountAnalysis(orderList);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

}
