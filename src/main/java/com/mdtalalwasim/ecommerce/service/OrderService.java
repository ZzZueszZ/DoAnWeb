package com.mdtalalwasim.ecommerce.service;

import com.mdtalalwasim.ecommerce.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Long userId);
    List<Order> getOrdersByUser(Long userId);
    List<Order> getPendingOrders();
    void approveOrder(Long orderId);
    void cancelOrder(Long orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByStatus(Order.OrderStatus status);
} 