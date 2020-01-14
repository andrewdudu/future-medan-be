package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.Review;

import java.util.List;

public interface ReviewService {

    List<Review> getAll();

    List<Review> getReviewByProductId(String productId);

    Review getReviewByUserIdAndProductId(String userId, String productId);

    Review save(Review review);
}
