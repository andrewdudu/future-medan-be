package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Purchase;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.PurchaseService;
import com.future.medan.backend.services.StorageService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FileControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private StorageService storageService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PurchaseService purchaseService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private String productIdSuccess, productIdNotFound, userId, token,
            purchaseIdSuccess, purchaseIdNotFound, filePath, fileName;

    private User user, user2;

    private Product product1, product2;

    private Purchase purchaseSuccess, purchaseNotFound, purchaseNull;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(new FileController(storageService, jwtTokenProvider, purchaseService)).build();

        this.mapper = new ObjectMapper();

        this.productIdSuccess = "AB-0001-0001";
        this.productIdNotFound = "AB-1110-1110";
        this.userId = "user-test-id";
        this.purchaseIdSuccess = "purchase-success";
        this.purchaseIdNotFound = "purchase-not-found";
        this.filePath = "/img/static";
        this.fileName = "image1";

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

        this.purchaseSuccess = Purchase.builder()
                .user(user)
                .merchant(user2)
                .orderId("order-1")
                .product(product1)
                .status("status")
                .build();
        this.purchaseNotFound = Purchase.builder()
                .user(user2)
                .merchant(user2)
                .orderId("order-2")
                .product(product2)
                .status("status-2")
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

    @Test
    public void testGetImage_Ok() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        byte[] expected = fileName.getBytes();

        when(storageService.loadImage(fileName)).thenReturn(expected);

        mockMvc.perform(get("/api/get-image/{filePath}", fileName))
                .andExpect(status().isOk());

        verify(storageService).loadImage(fileName);
    }

    @Test
    public void testGetPdf_Ok() throws Exception {
        byte[] content = fileName.getBytes();
        when(storageService.loadBook(fileName)).thenReturn(content);
        when(purchaseService.getByProductIdAndUserId(productIdSuccess, userId))
                .thenReturn(purchaseSuccess);

        mockMvc.perform(get("/api/get-pdf?file-path=" + fileName + "&product-id=" + productIdSuccess, filePath)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        verify(storageService).loadBook(fileName);
    }

    @Test
    public void testGetPdf_Unauthorized() throws Exception {
        when(purchaseService.getByProductIdAndUserId(productIdSuccess, userId))
                .thenReturn(null);

        mockMvc.perform(get("/api/get-pdf?file-path=" + fileName + "&product-id=" + productIdSuccess, filePath)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isUnauthorized());

        verify(purchaseService).getByProductIdAndUserId(productIdSuccess, userId);
    }

    @Test
    public void testIsPurchased_True() {
        Boolean expected = true;

        when(purchaseService.getByProductIdAndUserId(productIdSuccess, userId))
                .thenReturn(purchaseSuccess);

        Boolean actual = purchaseService.getByProductIdAndUserId(productIdSuccess, userId) != null;

        assertEquals(expected, actual);
    }

    @Test
    public void testIsPurchased_False() {
        when(purchaseService.getByProductIdAndUserId(productIdNotFound, userId))
            .thenReturn(null);

        Boolean actual = purchaseService.getByProductIdAndUserId(productIdNotFound, userId) != null;

        assertEquals(false, actual);
    }
}
