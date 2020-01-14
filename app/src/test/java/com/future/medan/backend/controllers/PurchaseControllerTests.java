package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.*;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.PurchaseWebRequest;
import com.future.medan.backend.payload.responses.*;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PurchaseControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PurchaseService purchaseService;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Purchase purchase;

    private User user, merchant;

    private Category category;

    private Product product;

    private String purchaseId, purchaseId2, token, userId, categoryId, orderId, merchantId, productId;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new PurchaseController(purchaseService, jwtTokenProvider)).build();

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

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(purchaseService);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Purchase> expected = Collections.singletonList(purchase);

        when(purchaseService.getAll()).thenReturn(expected);

        Response<List<PurchaseWebResponse>> responses = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/purchases").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(responses), json);
                });

        verify(purchaseService).getAll();
    }

    @Test
    public void testGetById_Ok() throws Exception {
        when(purchaseService.getById(purchaseId)).thenReturn(purchase);

        mockMvc.perform(get(ApiPath.PURCHASES + "/" + purchaseId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        verify(purchaseService).getById(purchaseId);
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        when(purchaseService.getById(purchaseId)).thenThrow(new ResourceNotFoundException("Purchase", "id", purchaseId));

        mockMvc.perform(get("/api/purchases/{id}", purchaseId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(purchaseService).getById(purchaseId);
    }

    @Test
    public void testGetByMerchantId_Ok() throws Exception {
        List<Purchase> expected = Collections.singletonList(purchase);

        when(purchaseService.getIncomingOrderByMerchantId(merchantId)).thenReturn(expected);

        Response<List<PurchaseWebResponse>> responses = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/merchant/purchases/{id}", merchantId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(responses), json);
                });

        verify(purchaseService).getIncomingOrderByMerchantId(merchantId);
    }

    @Test
    public void testGetByMerchantId_NotFound() throws Exception {
        when(purchaseService.getIncomingOrderByMerchantId(merchantId)).thenThrow(new ResourceNotFoundException("Merchant", "id", merchantId));

        mockMvc.perform(get("/api/merchant/purchases/{id}", merchantId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(purchaseService).getIncomingOrderByMerchantId(merchantId);
    }

    @Test
    public void testSave_Ok() throws Exception {
        PurchaseWebRequest request = new PurchaseWebRequest(new HashSet<>(Collections.singletonList(productId)));

        doNothing().when(purchaseService).save(request, userId);

        Response<SuccessWebResponse> response = ResponseHelper.ok(new SuccessWebResponse(true));

        mockMvc.perform(post("/api/purchases")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(purchaseService).save(request, userId);
    }

    @Test
    public void testSave_BadRequest() throws Exception {
        mockMvc.perform(post("/api/purchases")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("BAD REQUEST"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        doNothing().when(purchaseService).deleteById(purchaseId);

        mockMvc.perform(delete("/api/purchases/{id}", purchaseId))
                .andExpect(status().isOk());

        verify(purchaseService).deleteById(purchaseId);
    }

    @Test
    public void testDeleteById_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Purchase", "id", purchaseId)).when(purchaseService).deleteById(purchaseId);

        mockMvc.perform(delete("/api/purchases/{id}", purchaseId))
                .andExpect(status().isNotFound());

        verify(purchaseService).deleteById(purchaseId);
    }
}
