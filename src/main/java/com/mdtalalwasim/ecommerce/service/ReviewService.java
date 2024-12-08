package com.mdtalalwasim.ecommerce.service;

import com.mdtalalwasim.ecommerce.entity.Review;

public interface ReviewService {
    Review addReview(Long userId, Long productId, int rating, String comment);
    boolean hasUserPurchasedProduct(Long userId, Long productId);
    boolean hasUserReviewedProduct(Long userId, Long productId);
} 