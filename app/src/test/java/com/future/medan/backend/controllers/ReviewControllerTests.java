package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Review;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.ReviewWebRequest;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.ReviewWebResponse;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.ReviewService;
import com.future.medan.backend.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReviewControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper mapper;

    private String productIdSuccess, productIdNotFound, userId, token;

    private User user, user2;

    private Product product1, product2;

    private Review review1, review2;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        RestAssured.port = port;

        this.mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        this.mockMvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviewService, productService, userService, jwtTokenProvider)).build();

        this.productIdSuccess = "AB-0001-0001";
        this.productIdNotFound = "AB-1110-1110";
        this.userId = "user-test-id";

        this.user = User.builder()
                .description("Test Description")
                .email("test@example.com")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .name("Test Book")
                .password("Test")
                .roles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .status(true)
                .username("testusername")
                .build();
        this.user2 = User.builder()
                .description("Test Description")
                .email("test@example.com")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .name("Test Book")
                .password("Test")
                .roles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .status(true)
                .username("testusername")
                .build();

        this.product1 = Product.builder()
                .author("Test")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .description("Test")
                .name("Test")
                .price(new BigDecimal(123123))
                .sku("ABCD-0001")
                .pdf("HEA-0002.pdf")
                .hidden(false)
                .isbn("978-3-16-148410-0")
                .variant("ABCD-0001-0001")
                .build();
        this.product2 = Product.builder()
                .author("Test2")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .description("Test2")
                .name("Test2")
                .price(new BigDecimal(321321))
                .sku("ABCD-0002")
                .pdf("HEA-0003.pdf")
                .hidden(false)
                .isbn("978-3-16-148410-1")
                .variant("ABCD-0001-0002")
                .build();

        this.review1 = Review.builder()
                .user(user)
                .product(product1)
                .rating(5)
                .comment("OK")
                .build();
        this.review2 = Review.builder()
                .user(user2)
                .product(product1)
                .rating(1)
                .comment("KO")
                .build();

        this.token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    public void testGetReviewByProductId_Ok() throws Exception {
        List<Review> expected = Arrays.asList(review1, review2);

        when(reviewService.getReviewByProductId(productIdSuccess)).thenReturn(expected);

        Response<List<ReviewWebResponse>> responseExpected = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toReviewEntity)
                .collect(Collectors.toList())
        );

        mockMvc.perform(get("/api/review/{productId}", productIdSuccess)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo( actual ->{
                    String responseActual = actual.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(responseExpected), responseActual);
                });

        verify(reviewService).getReviewByProductId(productIdSuccess);
    }

    @Test
    public void testGetReviewByProductId_NotFound() throws Exception {
        when(reviewService.getReviewByProductId(productIdNotFound))
                .thenThrow(new ResourceNotFoundException("Review", "product id", productIdNotFound));

        mockMvc.perform(get("/api/review/{productId}", productIdNotFound)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(reviewService).getReviewByProductId(productIdNotFound);
    }

    @Test
    public void testGetReviewByUserIdAndProductId_Ok () throws Exception {
        Review expected = review1;

        when(reviewService.getReviewByUserIdAndProductId(userId, productIdSuccess)).thenReturn(expected);

        Response<ReviewWebResponse> responseExpected = ResponseHelper.ok(WebResponseConstructor.toReviewEntity(expected));

        mockMvc.perform(get("/api/my-review/{productId}", productIdSuccess)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo( actual ->{
                    String responseActual = actual.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(responseExpected), responseActual);
                });

        verify(reviewService).getReviewByUserIdAndProductId(userId, productIdSuccess);
    }

    @Test
    public void testGetReviewByUserIdAndProductId_NotFound() throws Exception {
        when(reviewService.getReviewByUserIdAndProductId(userId, productIdNotFound))
                .thenThrow(new ResourceNotFoundException("Review", "product id", productIdNotFound));

        mockMvc.perform(get("/api/my-review/{productId}", productIdNotFound)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(reviewService).getReviewByUserIdAndProductId(userId, productIdNotFound);
    }

    @Test
    public void testAddReview_Ok() throws Exception {
        Review expected = review1;

        when(reviewService.save(review1)).thenReturn(expected);
        when(productService.getById(productIdSuccess)).thenReturn(product1);
        when(userService.getById(userId)).thenReturn(user);

        Response<ReviewWebResponse> responseExpected = ResponseHelper.ok(
                WebResponseConstructor.toReviewEntity(expected)
        );

        ReviewWebRequest reviewWebRequest = new ReviewWebRequest();
        reviewWebRequest.setProductId(productIdSuccess);
        reviewWebRequest.setRating(5);
        reviewWebRequest.setComment("OK");

        mockMvc.perform(
                post("/api/review")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(reviewWebRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andDo( actual -> {
                    String responseActual = actual.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(responseExpected), responseActual);
                });

        verify(reviewService).save(review1);
        verify(productService).getById(productIdSuccess);
        verify(userService).getById(userId);
    }

    @Test
    public void testAddReview_BadRequest() throws Exception {
        mockMvc.perform(post("/api/review")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("Test for bad request"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddReview_ProductNotFound() throws Exception {
        when(productService.getById(productIdNotFound)).thenThrow(
                new ResourceNotFoundException("Product", "id", productIdNotFound)
        );

        ReviewWebRequest reviewWebRequest = new ReviewWebRequest();
        reviewWebRequest.setProductId(productIdNotFound);
        reviewWebRequest.setRating(1);
        reviewWebRequest.setComment("401");

        mockMvc.perform(
                post("/api/review")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(reviewWebRequest))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(productService).getById(productIdNotFound);
    }
}
