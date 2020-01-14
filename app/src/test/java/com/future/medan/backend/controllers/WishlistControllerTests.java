package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.*;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.WishlistWebRequest;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.payload.responses.WishlistWebResponse;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.UserService;
import com.future.medan.backend.services.WishlistService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WishlistControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private WishlistService wishlistService;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private User user, merchant;

    Product product;

    Category category;

    private Wishlist wishlist, wishlist2;

    private String wishlistId, wishlistId2, userId, userId2, categoryId, token;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new WishlistController(
                wishlistService,
                userService,
                productService,
                jwtTokenProvider
        )).build();

        this.wishlistId = "wishlist-id-1";
        this.wishlistId2 = "id-unavailable";
        this.userId = "syntialai";
        this.userId2 = "andrewwijaya";
        this.categoryId = "test-category-id";

        this.token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        mapper = new ObjectMapper();

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

        this.wishlist = Wishlist.builder()
                .products(new HashSet<>(Collections.singletonList(product)))
                .user(user)
                .build();

        this.wishlist2 = Wishlist.builder()
                .products(new HashSet<>(Collections.singletonList(product)))
                .user(user)
                .build();

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(wishlistService);
    }

    @Test
    public void testGetAll_Ok() throws Exception {
        List<Wishlist> expected = Arrays.asList(wishlist, wishlist2);

        when(wishlistService.getAll()).thenReturn(expected);

        Response<List<WishlistWebResponse>> response = ResponseHelper.ok(expected
                        .stream()
                        .map(WebResponseConstructor::toWebResponse)
                        .collect(Collectors.toList()));

        mockMvc.perform(get("/api/wishlists").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(wishlistService).getAll();
    }

    @Test
    public void testGetByUserId_Ok() throws Exception {
        Wishlist expected = wishlist;

        when(wishlistService.getByUserId(userId)).thenReturn(expected);

        Response<WishlistWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(expected));

        mockMvc.perform(get("/api/my-wishlists")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(wishlistService).getByUserId(userId);
    }

    @Test
    public void testGetByUserId_NotFound() throws Exception {
        when(wishlistService.getByUserId(userId)).thenThrow(new ResourceNotFoundException("Wishlist", "id", wishlistId));

        mockMvc.perform(get("/api/my-wishlists")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(wishlistService).getByUserId(userId);
    }

    @Test
    public void testSave_Ok() throws Exception {
        String productId = "product-id-test";

        when(userService.getById(userId)).thenReturn(user);
        when(productService.getById(productId)).thenReturn(product);
        when(wishlistService.save(user, product)).thenReturn(wishlist);

        Response<WishlistWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(wishlist));

        WishlistWebRequest request = new WishlistWebRequest(productId);

        mockMvc.perform(post("/api/wishlists")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).getById(userId);
        verify(productService).getById(productId);
        verify(wishlistService).save(user, product);
    }

    @Test
    public void testSave_BadRequest() throws Exception {
        mockMvc.perform(post("/api/wishlists")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("BAD REQUEST"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        String productId = "product-id-test";

        when(wishlistService.deleteByProductId(productId, userId)).thenReturn(wishlist);

        WishlistWebRequest request = new WishlistWebRequest(productId);

        mockMvc.perform(delete("/api/wishlists")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(wishlistService).deleteByProductId(productId, userId);
    }
}
