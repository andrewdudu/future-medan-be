package com.future.medan.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.*;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.CategoryWebRequest;
import com.future.medan.backend.payload.requests.ProductWebRequest;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.CategoryService;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.PurchaseService;
import com.future.medan.backend.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UserService userService;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private ProductService productService;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Product product, product2;

    private Purchase purchase;

    private User user, merchant;

    private Category category;

    private String productId, productId2, token, userId, purchaseId, categoryId;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService,
                categoryService,
                userService,
                purchaseService,
                jwtTokenProvider)).build();

        this.userId = "user-id-test";

        this.token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        mapper = new ObjectMapper();

        this.productId = "7892b1a2-0953-4071-9ffe-a5e193255585";

        this.productId2 = "id-unavailable";

        this.purchaseId = "purchase-id";

        this.categoryId = "category-id";

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
                .sku("string")
                .description("string")
                .price(new BigDecimal("100000"))
                .image("string")
                .author("string")
                .hidden(false)
                .merchant(merchant)
                .category(category)
                .build();

        this.purchase = Purchase.builder()
                .merchant(merchant)
                .orderId("TEST")
                .product(product)
                .status("TEST")
                .user(user)
                .build();

        this.product2 = Product.builder()
                .name("string")
                .sku("string")
                .description("string")
                .price(new BigDecimal("100000"))
                .image("string")
                .author("string")
                .hidden(false)
                .merchant(merchant)
                .category(category)
                .build();

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void testGetAll_Ok() throws Exception {
        List<Product> expected = Arrays.asList(product, product2);

        when(productService.getAll()).thenReturn(expected);

        Response<List<ProductWebResponse>> response = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/all-products").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(productService).getAll();
    }

    @Test
    public void getAllWithoutHidden() throws Exception {
        List<Product> expected = Arrays.asList(product, product2);

        when(productService.getAllWithoutHidden()).thenReturn(expected);

        Response<List<ProductWebResponse>> response = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/products").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(productService).getAllWithoutHidden();
    }

    @Test
    public void testFindPaginated_Ok() throws Exception {
        Page<Product> expected = new PageImpl<>(Arrays.asList(product, product2));

        when(productService.findPaginated(1, 2)).thenReturn(expected);

        PaginationResponse<List<ProductWebResponse>> response = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()), 2, 1);

        mockMvc.perform(get("/api/products/paginate?page=1&size=2").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(productService).findPaginated(1, 2);
    }

    @Test
    public void testGetMyProducts() throws Exception {
        Set<Purchase> purchasedProducts = new HashSet<>(Collections.singletonList(purchase));

        when(purchaseService.getPurchasedProduct(userId)).thenReturn(purchasedProducts);

        Response<List<PurchaseWebResponse>> response = ResponseHelper.ok(purchasedProducts
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/my-products").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(purchaseService).getPurchasedProduct(userId);
    }

    @Test
    public void testGetById_Ok() throws Exception {
        Product expected = product;

        when(productService.getById(productId)).thenReturn(expected);

        Response<ProductByIdWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toProductByIdWebResponse(expected));

        mockMvc.perform(get("/api/products/{id}", productId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(productService).getById(productId);
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        when(productService.getById(productId)).thenThrow(new ResourceNotFoundException("Product", "id", productId));

        mockMvc.perform(get("/api/products/{id}", productId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(productService).getById(productId);
    }

    @Test
    public void testHide_Ok() throws Exception {
        Product expected = product;

        when(productService.hide(productId)).thenReturn(expected);

        Response<ProductWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(expected));

        mockMvc.perform(post("/api/products/hide/{id}", productId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(productService).hide(productId);
    }

    @Test
    public void testHide_NotFound() throws Exception {
        when(productService.hide(productId)).thenThrow(new ResourceNotFoundException("Product", "id", productId));

        mockMvc.perform(post("/api/products/hide/{id}", productId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(productService).hide(productId);
    }

    @Test
    public void testSave_Ok() throws Exception {
        Product expected = product;

        when(productService.save(product)).thenReturn(expected);
        when(userService.getById(userId)).thenReturn(merchant);
        when(categoryService.getById(categoryId)).thenReturn(category);

        Response<ProductWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(expected));

        ProductWebRequest request = new ProductWebRequest("Test", "Test", new BigDecimal(100), "Test", categoryId, "Test", "Test");

        mockMvc.perform(post("/api/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(productService).save(product);
    }

    @Test
    public void testSave_BadRequest() throws Exception {
        mockMvc.perform(post("/api/categories")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ProductWebRequest(null, "Test", BigDecimal.valueOf(100), "Test", "Test", "Test", "Test"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEditById_Ok() {

    }

    @Test
    public void testEditById_BadRequest() {

    }

    @Test
    public void testEditById_NotFound() {

    }

    @Test
    public void deleteById_Ok() {

    }

    @Test
    public void deleteById_NotFound() {

    }
}
