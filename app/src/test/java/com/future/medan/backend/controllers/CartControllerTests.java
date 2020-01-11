package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Cart;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.CartWebRequest;
import com.future.medan.backend.payload.responses.CartWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.CartService;
import com.future.medan.backend.services.ProductService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CartControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Cart cart, cart2;

    private String cartId, cartId2, token, userId, productId, notFoundProductId;

    private Product product;

    private UserDetails userDetails;

    private User user;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new CartController(cartService, userService, productService, jwtTokenProvider)).build();

        mapper = new ObjectMapper();

        this.userId = "user-test-id";
        this.notFoundProductId = "test-random-id";
        this.productId = "product-test-id";

        token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

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

        this.product = Product.builder()
                .author("Test")
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .description("Test")
                .name("Test")
                .price(new BigDecimal(123123))
                .sku("ABCD-0001")
                .pdf("HEA-0002.pdf")
                .hidden(false)
                .variant("ABCD-0001-0001")
                .build();

        this.cartId = "ABCD";

        this.cartId2 = "id-unavailable";

        this.cart = Cart.builder()
                .products(new HashSet<>(Collections.singletonList(product)))
                .user(user)
                .build();

        this.cart2 = Cart.builder()
                .products(new HashSet<>(Collections.singletonList(product)))
                .user(user)
                .build();

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(cartService);
    }

    @Test
    public void testGetCart_Ok() throws Exception {
        Cart expected = cart;

        when(cartService.getByUserId(userId)).thenReturn(expected);

        Response<CartWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(expected));

        mockMvc.perform(get("/api/carts").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(cartService).getByUserId(userId);
    }

    @Test
    public void testGetCart_NotFound() throws Exception {
        when(cartService.getByUserId(userId)).thenThrow(new ResourceNotFoundException("Cart", "user id", userId));

        mockMvc.perform(get("/api/carts").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

        verify(cartService).getByUserId(userId);
    }

    @Test
    public void testSave_Ok() throws Exception {
        Cart expected = cart;

        when(userService.getById(userId)).thenReturn(user);
        when(productService.getById(productId)).thenReturn(product);
        when(cartService.save(user, product)).thenReturn(expected);

        Response<CartWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(expected));

        CartWebRequest request = new CartWebRequest(productId);

        mockMvc.perform(post("/api/carts")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(cartService).save(user, product);
        verify(productService).getById(productId);
        verify(userService).getById(userId);
    }

    @Test
    public void testSave_BadRequest() throws Exception {
        mockMvc.perform(post("/api/carts")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSave_NotFound() throws Exception {
        Cart expected = cart;

        when(userService.getById(userId)).thenReturn(user);
        when(productService.getById(notFoundProductId)).thenThrow(new ResourceNotFoundException("Product", "id", productId));
        when(cartService.save(user, product)).thenReturn(expected);

        CartWebRequest request = new CartWebRequest(notFoundProductId);

        mockMvc.perform(post("/api/carts")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(userService).getById(userId);
        verify(productService).getById(notFoundProductId);
    }

    @Test
    public void testDeleteById_Ok() throws Exception {
        doNothing().when(cartService).deleteByProductId(productId, userId);

        CartWebRequest request = new CartWebRequest(productId);

        mockMvc.perform(delete("/api/carts")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .content(mapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(cartService).deleteByProductId(productId, userId);
    }

    @Test
    public void testDeleteById_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User", "id", userId)).when(cartService).deleteByProductId(productId, userId);

        CartWebRequest request = new CartWebRequest(productId);

        mockMvc.perform(delete("/api/carts")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .content(mapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(cartService).deleteByProductId(productId, userId);
    }

    @Test
    public void testDeleteById_BadRequest() throws Exception {
        mockMvc.perform(delete("/api/carts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content("Is not a json")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
