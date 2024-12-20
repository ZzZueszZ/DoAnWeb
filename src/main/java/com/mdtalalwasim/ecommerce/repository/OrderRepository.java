package com.mdtalalwasim.ecommerce.repository;

import com.mdtalalwasim.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(Order.OrderStatus status);

    boolean existsByUserIdAndOrderItemsProductIdAndStatus(
            Long userId, Long productId, Order.OrderStatus status);
}