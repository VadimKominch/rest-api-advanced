package com.epam.esm.service;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderService {
    private OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<Order> getAll() {
        return orderDao.getAllOrders();
    }

    public Order getById(Integer id) {
        return orderDao.getOrderById(id);
    }

    public boolean save(Order entity) {
        if (entity == null) {
            return false;
        }
        orderDao.save(entity);
        return true;
    }

    public List<Order> getOrderByUserId(Integer userId,Integer pageNumber,Integer pageSize) {
        return orderDao.getOrderByUserId(userId,pageNumber,pageSize);
    }

    public boolean delete(Integer id) {
        return orderDao.delete(id);
    }

    public void saveAll(List<Order> certificates) {
        certificates.forEach(el -> orderDao.save(el));
    }

    public Long getUserOrdersCount(int userId) {
        return orderDao.getUserOrdersCount(userId);
    }

}
