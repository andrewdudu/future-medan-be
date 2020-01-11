package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Review;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.repositories.ReviewRepository;
import com.future.medan.backend.services.ReviewService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewImplTests {

    @Mock
    private ReviewRepository reviewRepository;

    private ReviewService reviewService;

    private User user1, user2;
    private Product productSuccess, productNotFound;
    private Review reviewSuccess, reviewNotFound;

    private String reviewIdSuccess, reviewIdNotFound,
                   productIdSuccess, productIdNotFound;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.reviewService = new ReviewServiceImpl(reviewRepository);

        this.reviewIdSuccess = "review-id-test";
        this.reviewIdNotFound = "review-id-test-failed";
        this.productIdSuccess = "product-id-test";
        this.productIdNotFound = "product-id-test-failed";

        this.user1 = User.builder()
                .name("User Test")
                .username("user-test")
                .email("test@example.com")
                .description("")
                .password("Test")
                .status(true)
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .roles(new HashSet<>(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .build();
        this.user2 = User.builder()
                .name("User Test 2")
                .username("user-test-2")
                .email("test2@example.com")
                .description("")
                .password("Test2")
                .status(true)
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .roles(new HashSet<>(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .build();

        this.productSuccess = Product.builder()
                .name("Product Test")
                .merchant(user1)
                .author("Author 1")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .sku("sku-1")
                .description("one")
                .price(new BigDecimal(50000))
                .hidden(false)
                .build();
        this.productNotFound = Product.builder()
                .name("Product Test 2")
                .merchant(user2)
                .author("Author 2")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .sku("sku-2")
                .description("two")
                .price(new BigDecimal(10000))
                .hidden(false)
                .build();

        this.reviewSuccess = Review.builder()
                .user(user1)
                .product(productSuccess)
                .rating(5)
                .build();
        this.reviewNotFound = Review.builder()
                .user(user2)
                .product(productNotFound)
                .rating(3)
                .comment("Average")
                .build();
    }

    @Test
    public void testGetAll() {
        List<Review> expected = Arrays.asList(reviewSuccess, reviewNotFound);

        when(reviewRepository.findAll()).thenReturn(expected);
        List<Review> actual = reviewService.getAll();

        assertEquals(expected, actual);
    }

//    @Test
//    public void testGetReviewByProductId_Ok(){
//
//    }
}
