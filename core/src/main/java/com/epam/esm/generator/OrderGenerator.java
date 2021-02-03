package com.epam.esm.generator;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderGenerator {
    public static List<Order> getOrders(int count) {
        return IntStream.range(0,count).mapToObj(el->new Order()).collect(Collectors.toList());
    }
}
