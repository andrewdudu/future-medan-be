package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Review;
import com.future.medan.backend.repositories.ReviewRepository;
import com.future.medan.backend.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewByProductId(String productId) {
        return reviewRepository.getAllByProductId(productId);
    }

    @Override
    public Review getReviewByUserIdAndProductId(String userId, String productId) {
        return reviewRepository.getByUserIdAndAndProductId(userId, productId);
    }

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }
}
