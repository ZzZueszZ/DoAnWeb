package com.mdtalalwasim.ecommerce.service.impl;

import com.mdtalalwasim.ecommerce.entity.*;
import com.mdtalalwasim.ecommerce.repository.*;
import com.mdtalalwasim.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public Review addReview(Long userId, Long productId, int rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Check if user has purchased product
        if (!hasUserPurchasedProduct(userId, productId)) {
            throw new RuntimeException("You can only review products you have purchased");
        }

        // Check if user has already reviewed this product
        if (hasUserReviewedProduct(userId, productId)) {
            throw new RuntimeException("You have already reviewed this product");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        return orderRepository.existsByUserIdAndOrderItemsProductIdAndStatus(
            userId, productId, Order.OrderStatus.CONFIRMED);
    }

    @Override
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        return reviewRepository.existsByUserIdAndProductId(userId, productId);
    }
} 