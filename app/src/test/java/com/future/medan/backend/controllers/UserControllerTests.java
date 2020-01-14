package com.future.medan.backend.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.exceptions.AuthenticationFailException;
import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Category;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.ForgotPasswordWebRequest;
import com.future.medan.backend.payload.requests.ResetPasswordWebRequest;
import com.future.medan.backend.payload.requests.UserWebRequest;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.security.JwtTokenProvider;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private User user, user2;

    private Product product;

    private Category category;

    private String userId, userId2, token, categoryId;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, productService, jwtTokenProvider)).build();

        this.categoryId = "category-id-test";

        this.token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        mapper = new ObjectMapper();

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
                .merchant(user)
                .category(category)
                .build();

        this.userId = "user1-id";
        this.userId2 = "id-unavailable";

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

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetAll_Ok() throws Exception {
        List<User> expected = Arrays.asList(user, user2);

        when(userService.getAll()).thenReturn(expected);

        Response<List<UserWebResponse>> responses = ResponseHelper.ok(expected
            .stream()
            .map(WebResponseConstructor::toWebResponse)
            .collect(Collectors.toList()));

        mockMvc.perform(get("/api/users").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(responses), json);
                });

        verify(userService).getAll();
    }

    @Test
    public void testFindPaginatedUser_Ok() throws Exception {
        Page<User> expected = new PageImpl<>(Arrays.asList(user, user2));

        when(userService.findPaginatedUser(1, 2)).thenReturn(expected);

        PaginationResponse<List<UserWebResponse>> response = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()), 2, 1);

        mockMvc.perform(get("/api/users/paginate?page=1&size=2").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).findPaginatedUser(1, 2);
    }

    @Test
    public void testFindPaginatedMerchant_Ok() throws Exception {
        Page<User> expected = new PageImpl<>(Arrays.asList(user, user2));

        when(userService.findPaginatedMerchant(1, 2)).thenReturn(expected);

        PaginationResponse<List<UserWebResponse>> response = ResponseHelper.ok(expected
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()), 2, 1);

        mockMvc.perform(get("/api/merchants/paginate?page=1&size=2").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).findPaginatedMerchant(1, 2);
    }

    @Test
    public void testGetById_Ok() throws Exception {
        when(userService.getById(userId)).thenReturn(user);

        Response<UserWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(user));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).getById(userId);
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        when(userService.getById(userId)).thenThrow(new ResourceNotFoundException("User", "id", userId));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService).getById(userId);
    }
    @Test
    public void testGetByMerchantId_Ok() throws Exception {
        when(userService.getMerchantById(userId)).thenReturn(user);
        when(productService.getByMerchantId(userId)).thenReturn(Collections.singletonList(product));

        Response<MerchantWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(user, Collections.singletonList(product)));

        mockMvc.perform(get("/api/merchant/{id}", userId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).getMerchantById(userId);
        verify(productService).getByMerchantId(userId);
    }

    @Test
    public void testGetByMerchantId_NotFound() throws Exception {
        when(userService.getMerchantById(userId)).thenThrow(new ResourceNotFoundException("Merchant", "id", userId));

        mockMvc.perform(get("/api/merchant/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService).getMerchantById(userId);
    }


    @Test
    public void testBlockUser_Ok() throws Exception {
        when(userService.block(userId)).thenReturn(user);

        Response<UserWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(user));

        mockMvc.perform(post("/api/users/{id}", userId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).block(userId);
    }

    @Test
    public void testBlockUser_NotFound() throws Exception {
        when(userService.block(userId)).thenThrow(new ResourceNotFoundException("User", "id", userId));

        mockMvc.perform(post("/api/users/{id}", userId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(userService).block(userId);
    }

    @Test
    public void testForgotPassword_Ok() throws Exception {
        String email = "test@example.com";

        ForgotPasswordWebRequest request = new ForgotPasswordWebRequest(email);
        Response<ForgotPasswordWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toForgotPasswordWebResponse(true));

        when(userService.requestPasswordReset(email)).thenReturn(true);

        mockMvc.perform(post("/api/forgot-password")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).requestPasswordReset(email);
    }

    @Test
    public void testResetPassword_Ok() throws Exception {
        String password = "test-password";

        ResetPasswordWebRequest request = new ResetPasswordWebRequest(token, password);
        Response<ResetPasswordWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toResetPasswordWebResponse(true));

        when(userService.resetPassword(token, password)).thenReturn(true);

        mockMvc.perform(post("/api/reset-password")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).resetPassword(token, password);
    }

    @Test
    public void testEditById_Ok() throws Exception {
        String name = "test-name", description = "test-description";

        UserWebRequest request = new UserWebRequest(name, description);
        user.setDescription(description);
        user.setName(name);
        Response<UserWebResponse> response = ResponseHelper.ok(WebResponseConstructor.toWebResponse(user));

        when(userService.save(request, userId)).thenReturn(user);

        mockMvc.perform(put("/api/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userService).save(request, userId);
    }

    @Test
    public void testEditById_BadRequest() throws Exception {
        mockMvc.perform(put("/api/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("BAD REQUEST"))
                .andExpect(status().isBadRequest());
    }
}
