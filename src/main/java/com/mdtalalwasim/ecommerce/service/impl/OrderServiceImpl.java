package com.mdtalalwasim.ecommerce.service.impl;

import com.mdtalalwasim.ecommerce.entity.Cart;
import com.mdtalalwasim.ecommerce.entity.Order;
import com.mdtalalwasim.ecommerce.entity.OrderItem;
import com.mdtalalwasim.ecommerce.repository.OrderRepository;
import com.mdtalalwasim.ecommerce.repository.OrderItemRepository;
import com.mdtalalwasim.ecommerce.repository.CartRepository;
import com.mdtalalwasim.ecommerce.service.CartService;
import com.mdtalalwasim.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;
    
    @Override
    @Transactional
    public Order createOrder(Long userId) {
        try {
            // Lấy cart items
            List<Cart> cartItems = cartService.getCartsByUser(userId);
            if(cartItems.isEmpty()) {
                throw new RuntimeException("Cart is empty");
            }
            
            // Tính tổng tiền
            double totalPrice = cartItems.stream()
                .mapToDouble(cart -> cart.getProduct().getDiscountPrice() * cart.getQuantity())
                .sum();
            
            // Tạo order
            Order order = new Order();
            order.setUser(cartItems.get(0).getUser());
            order.setTotalPrice(totalPrice);
            order.setStatus(Order.OrderStatus.PENDING);
            order = orderRepository.save(order);
            
            // Tạo order items
            List<OrderItem> orderItems = new ArrayList<>();
            for(Cart cart : cartItems) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(cart.getProduct());
                item.setQuantity(cart.getQuantity());
                item.setPrice(cart.getProduct().getDiscountPrice());
                orderItems.add(orderItemRepository.save(item));
            }
            order.setOrderItems(orderItems);
            
            // Xóa cart sau khi tạo order
            cartRepository.deleteByUserId(userId);
            
            return order;
        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }
    
    @Override
    public List<Order> getOrdersByUser(Long userId) {
        try {
            return orderRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error getting user orders: " + e.getMessage());
        }
    }
    
    @Override
    public List<Order> getPendingOrders() {
        try {
            return orderRepository.findByStatus(Order.OrderStatus.PENDING);
        } catch (Exception e) {
            throw new RuntimeException("Error getting pending orders: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void approveOrder(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setStatus(Order.OrderStatus.CONFIRMED);
            orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error approving order: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setStatus(Order.OrderStatus.CANCELLED);
            orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error cancelling order: " + e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
} 