package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Review;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.requests.ReviewWebRequest;
import com.future.medan.backend.payload.requests.WebRequestConstructor;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.ReviewWebResponse;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.ReviewService;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class ReviewController {

    private ReviewService reviewService;

    private ProductService productService;

    private UserService userService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            ProductService productService,
                            UserService userService,
                            JwtTokenProvider jwtTokenProvider) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/api/review/{productId}")
    @Transactional
    public Response<List<ReviewWebResponse>> getReviewByProductId(@PathVariable String productId) {
        return ResponseHelper.ok(reviewService.getReviewByProductId(productId)
                .stream()
                .map(WebResponseConstructor::toReviewEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/review/{userId}/{productId}")
    public Response<ReviewWebResponse> getReviewByUserIdAndProductId(@PathVariable String userId, @PathVariable String productId) {
        return ResponseHelper.ok(WebResponseConstructor.toReviewEntity(reviewService.getReviewByUserIdAndProductId(userId, productId)));
    }

    @PostMapping("/api/review")
    public Response<ReviewWebResponse> addReview(@Validated @RequestBody ReviewWebRequest reviewWebRequest, @RequestHeader("Authorization") String bearerToken) {
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        Product product = productService.getById(reviewWebRequest.getProductId());
        User user = userService.getById(jwtTokenProvider.getUserIdFromJWT(token));



        Review review = reviewService.save(WebRequestConstructor.toReviewEntity(reviewWebRequest, user, product));

        return ResponseHelper.ok(WebResponseConstructor.toReviewEntity(review));
    }
}
