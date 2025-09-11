package com.test.task.trainee.stream;

import com.test.task.trainee.stream.entities.Customer;
import com.test.task.trainee.stream.entities.Order;
import com.test.task.trainee.stream.entities.OrderItem;
import com.test.task.trainee.stream.entities.OrderStatus;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MetricAnalyser {

    public List<String> uniqueCitiesAnalysis(List<Order> orderList) {
        List<String> result =
                orderList.stream()
                        .map(Order::getCustomer)
                        .map(Customer::getCity)
                        .filter(city -> city!=null)
                        .distinct()
                        .collect(Collectors.toList());
        return result;
    }

    public double totalIncomeAnalysis(List<Order> orderList) {
        double result =
                orderList.stream()
                        .map(Order::getItems)
                        .flatMap(items -> items.stream())
                        .collect(Collectors.summingDouble(OrderItem::getPrice));
        return result;
    }

    public String mostPopularProductBySalesAnalysis(List<Order> orderList) {
        String interresult =
                String.valueOf(orderList.stream()
                        .map(Order::getItems)
                        .flatMap(items -> items.stream())
                        .map(items -> items.getProductName())
                        .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparingLong(Map.Entry::getValue))
                        .map(Map.Entry::getKey));
        String result = interresult.substring(interresult.indexOf('[') + 1, interresult.lastIndexOf(']'));
        return result;
    }

    public double averageCheckAmountForDeliveredAnalysis(List<Order> orderList) {
        Double result =
                orderList.stream()
                        .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                        .map(Order::getItems)
                        .flatMap(items -> items.stream())
                        .mapToDouble(OrderItem::getPrice)
                        .average()
                        .getAsDouble();
        return result;
    }

    public List<Customer> customersWithHighOrdersCountAnalysis(List<Order> orderList){
        List<Customer> resultList =
                orderList.stream()
                        .map(Order::getCustomer)
                        .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparingLong(Map.Entry::getValue))
                        .map(Map.Entry::getKey)
                        .stream().toList();
        return resultList;
    }

}
