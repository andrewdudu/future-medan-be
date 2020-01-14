package com.future.medan.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.LoginWebRequest;
import com.future.medan.backend.payload.requests.SignUpWebRequest;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.repositories.RoleRepository;
import com.future.medan.backend.repositories.UserRepository;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.security.UserPrincipal;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTests {

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    User user;

    String token, userId, username, password, email;

    @Before
    public void setup() {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthenticationController(authenticationManager,
                        userRepository,
                        roleRepository,
                        passwordEncoder,
                        jwtTokenProvider))
                .build();

        this.userId = "user-id-test";
        this.username = "username";
        this.password = "password-test";
        this.email = "test@example.com";

        this.token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        this.user = User.builder()
                .description("Test Description")
                .email(email)
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .name("Test Book")
                .password(password)
                .roles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .status(true)
                .username(username)
                .build();

        mapper = new ObjectMapper();

        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userId);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(userRepository, roleRepository);
    }

    @Test
    public void testAuthenticateUser_Ok() throws Exception {
        LoginWebRequest request = new LoginWebRequest(username, password);
        Authentication auth = mock(Authentication.class);

        JwtAuthenticationResponse response = new JwtAuthenticationResponse(
                userId,
                user.getName(),
                username,
                user.getEmail(),
                token,
                user.getRoles().stream().map(role ->
                        new SimpleGrantedAuthority(role.getName().name())
                ).collect(Collectors.toList())
        );

        UserPrincipal userPrincipal = new UserPrincipal(
                userId,
                "Test Book",
                username,
                true,
                "test@example.com",
                password,
                user.getRoles().stream().map(role ->
                        new SimpleGrantedAuthority(role.getName().name())
                ).collect(Collectors.toList())
        );

        when(jwtTokenProvider.generateToken(auth)).thenReturn(token);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userPrincipal);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });
    }

    @Test
    public void testAuthenticateUser_Unauthorized() throws Exception {
        LoginWebRequest request = new LoginWebRequest(username, password);
        Authentication auth = mock(Authentication.class);

        UserPrincipal userPrincipal = new UserPrincipal(
                userId,
                "Test Book",
                username,
                false,
                "test@example.com",
                password,
                user.getRoles().stream().map(role ->
                        new SimpleGrantedAuthority(role.getName().name())
                ).collect(Collectors.toList())
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userPrincipal);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testValidateAdminToken_Ok() throws Exception {
        Response<AuthenticationApiResponse> response = ResponseHelper.ok(WebResponseConstructor.toValidateToken(true));

        mockMvc.perform(post("/api/validate-admin-token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });
    }

    @Test
    public void testValidateUserToken_Ok() throws Exception {
        Response<AuthenticationApiResponse> response = ResponseHelper.ok(WebResponseConstructor.toValidateToken(true));

        mockMvc.perform(post("/api/validate-user-token").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });
    }

    @Test
    public void testValidateMerchantToken_Ok() throws Exception {
        Response<AuthenticationApiResponse> response = ResponseHelper.ok(WebResponseConstructor.toValidateToken(true));

        mockMvc.perform(post("/api/validate-merchant-token").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });
    }

    @Test
    public void testRegisterMerchant_Ok() throws Exception {
        User userTest = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .status(true)
                .roles(Collections.singleton(new Role(RoleEnum.ROLE_MERCHANT)))
                .build();

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(roleRepository.findByName(RoleEnum.ROLE_MERCHANT)).thenReturn(java.util.Optional.of(new Role(RoleEnum.ROLE_MERCHANT)));
        when(userRepository.save(userTest)).thenReturn(userTest);

        AuthenticationApiResponse response = new AuthenticationApiResponse(true, "User registered successfully");

        SignUpWebRequest request = new SignUpWebRequest(user.getName(), username, email, password);

        mockMvc.perform(post("/api/merchant/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userRepository).existsByEmail(email);
        verify(userRepository).existsByUsername(username);
        verify(passwordEncoder).encode(password);
        verify(roleRepository).findByName(RoleEnum.ROLE_MERCHANT);
        verify(userRepository).save(userTest);
    }

    @Test
    public void testRegisterMerchantUsername_BadRequest() throws Exception {
        when(userRepository.existsByUsername(username)).thenReturn(true);

        SignUpWebRequest request = new SignUpWebRequest(user.getName(), username, email, password);

        mockMvc.perform(post("/api/merchant/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userRepository).existsByUsername(username);
    }

    @Test
    public void testRegisterMerchantEmail_BadRequest() throws Exception {
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        SignUpWebRequest request = new SignUpWebRequest(user.getName(), username, email, password);

        mockMvc.perform(post("/api/merchant/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userRepository).existsByUsername(username);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    public void testRegisterUser_Ok() throws Exception {
        User userTest = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .status(true)
                .roles(Collections.singleton(new Role(RoleEnum.ROLE_USER)))
                .build();

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(java.util.Optional.of(new Role(RoleEnum.ROLE_USER)));
        when(userRepository.save(userTest)).thenReturn(userTest);

        AuthenticationApiResponse response = new AuthenticationApiResponse(true, "User registered successfully");

        SignUpWebRequest request = new SignUpWebRequest(user.getName(), username, email, password);

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    assertEquals(mapper.writeValueAsString(response), json);
                });

        verify(userRepository).existsByEmail(email);
        verify(userRepository).existsByUsername(username);
        verify(passwordEncoder).encode(password);
        verify(roleRepository).findByName(RoleEnum.ROLE_USER);
        verify(userRepository).save(userTest);
    }

    @Test
    public void testRegisterUserUsername_BadRequest() throws Exception {
        when(userRepository.existsByUsername(username)).thenReturn(true);

        SignUpWebRequest request = new SignUpWebRequest(user.getName(), username, email, password);

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userRepository).existsByUsername(username);
    }

    @Test
    public void testRegisterUserEmail_BadRequest() throws Exception {
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        SignUpWebRequest request = new SignUpWebRequest(user.getName(), username, email, password);

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userRepository).existsByUsername(username);
        verify(userRepository).existsByEmail(email);
    }
}
