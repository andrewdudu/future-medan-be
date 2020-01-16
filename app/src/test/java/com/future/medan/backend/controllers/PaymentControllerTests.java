package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.models.entity.*;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.ApprovePurchaseWebRequest;
import com.future.medan.backend.payload.requests.PaymentWebRequest;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.SuccessWebResponse;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.PurchaseService;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PaymentControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    User user, merchant;

    Product product;

    Category category;

    Purchase purchase, purchase2;

    String token, categoryId, purchaseId, productId, orderId, purchaseId2, merchantId, userId;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(purchaseService, jwtTokenProvider)).build();

        this.userId = "user-id-test";

        this.token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        mapper = new ObjectMapper();

        this.purchaseId = "ABCD";
        this.productId = "product-id";
        this.orderId = "order-id";
        this.purchaseId2 = "id-unavailable";
        this.merchantId = "merchant-id";

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

        this.merchant = User.builder()
                .description("Test Description")
                .email("test@example.com")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .name("Test Book")
                .password("Test")
                .roles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .status(true)
                .username("testusername")
                .build();

        this.category = Category.builder()
                .hidden(false)
                .image("TEST")
                .name("TEST")
                .description("TEST")
                .build();

        this.category.setId(categoryId);

        this.product = Product.builder()
                .name("string")
                .description("string")
                .price(new BigDecimal("100000"))
                .image("string")
                .pdf("test")
                .author("string")
                .isbn("test")
                .hidden(false)
                .merchant(merchant)
                .category(category)
                .build();

        this.purchase = Purchase.builder()
                .user(user)
                .product(product)
                .merchant(merchant)
                .orderId(orderId)
                .status("WAITING")
                .build();

        this.purchase2 = Purchase.builder()
                .user(user)
                .product(product)
                .merchant(merchant)
                .orderId(orderId)
                .status("WAITING")
                .build();

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(purchaseService);
    }

    @Test
    public void testPayment() throws Exception {
        PaymentWebRequest request = new PaymentWebRequest(orderId);
        Set<Purchase> purchases = new HashSet<>(Collections.singletonList(purchase));
        Response<SuccessWebResponse> response = ResponseHelper.ok(new SuccessWebResponse(true));

        when(purchaseService.save(purchase)).thenReturn(purchase);
        when(purchaseService.getByOrderId(orderId)).thenReturn(purchases);


        mockMvc.perform(post("/api/payment")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(purchaseService).save(purchase);
        verify(purchaseService).getByOrderId(orderId);
    }

    @Test
    public void testApproved() throws Exception {
        ApprovePurchaseWebRequest request = new ApprovePurchaseWebRequest(productId, orderId);

        when(purchaseService.approveByOrderIdAndProductIdAndMerchantId(orderId, productId, userId)).thenReturn(true);

        Response<SuccessWebResponse> response = ResponseHelper.ok(new SuccessWebResponse(true));

        mockMvc.perform(post("/api/approved")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(purchaseService).approveByOrderIdAndProductIdAndMerchantId(orderId, productId, userId);
    }
}
