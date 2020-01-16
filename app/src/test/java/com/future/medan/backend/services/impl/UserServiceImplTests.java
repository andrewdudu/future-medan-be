package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.PasswordResetToken;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.UserWebRequest;
import com.future.medan.backend.repositories.PasswordResetTokenRepository;
import com.future.medan.backend.repositories.RoleRepository;
import com.future.medan.backend.repositories.UserRepository;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.MailService;
import com.future.medan.backend.services.StorageService;
import com.future.medan.backend.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTests {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private MailService mailService;

    @Mock
    private StorageService storageService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private User user, user2;
    private String findId, findId2;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                jwtTokenProvider,
                passwordResetTokenRepository,
                mailService,
                storageService,
                passwordEncoder);

        this.findId = "user1-id";
        this.findId2 = "id-unavailable";

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
    }

    @Test
    public void testGetAll(){
        List<User> expected = Arrays.asList(user, user2);

        when(userRepository.findAll()).thenReturn(expected);
        List<User> actual = userService.getAll();

        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasItem(user));
        assertThat(actual, hasItem(user2));
    }

    @Test
    public void testGetById_OK(){
        when(userRepository.findById(findId)).thenReturn(Optional.of(user2));

        assertEquals(userService.getById(findId), user2);
    }

    @Test
    public void testFindPaginatedUser_Ok() {
        Pageable paging = PageRequest.of(1, 2);
        Role role = new Role(RoleEnum.ROLE_USER);
        Page<User> expected = new PageImpl<>(Arrays.asList(user, user2));

        when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.findAllByRoles(role, paging)).thenReturn(expected);

        assertEquals(expected, userService.findPaginatedUser(1, 2));
    }

    @Test
    public void testFindPaginatedMerchant_Ok() {
        Pageable paging = PageRequest.of(1, 2);
        Role role = new Role(RoleEnum.ROLE_MERCHANT);
        Page<User> expected = new PageImpl<>(Arrays.asList(user, user2));

        when(roleRepository.findByName(RoleEnum.ROLE_MERCHANT)).thenReturn(Optional.of(role));
        when(userRepository.findAllByRoles(role, paging)).thenReturn(expected);

        assertEquals(expected, userService.findPaginatedMerchant(1, 2));
    }

    @Test
    public void testGetMerchantById() {
        user.setRoles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_MERCHANT))));
        when(userRepository.findById(findId)).thenReturn(Optional.ofNullable(user));

        assertEquals(user, userService.getMerchantById(findId));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetMerchantById_NotFound() {
        user.setRoles(new HashSet(Collections.singleton(new Role(RoleEnum.ROLE_MERCHANT))));

        assertEquals(user, userService.getMerchantById(findId));
    }

    @Test
    public void testSave(){
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThat(userService.save(user), is(notNullValue()));
    }

    @Test
    public void testEditById_OK() throws IOException {
        UserWebRequest request = new UserWebRequest(user.getName(), user.getDescription(), user.getImage());

        when(userRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(userRepository.findById(findId)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.save(request, findId));
    }

    @Test
    public void testBlock_Ok() {
        when(userRepository.findById(findId)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.block(findId));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testBlock_NotFound() {
        when(userRepository.findById(findId)).thenReturn(Optional.empty());

        userService.block(findId);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_OK(){
        when(userRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(userRepository.findById(findId))
                .thenThrow(new ResourceNotFoundException("User", "id", findId2));

        userService.deleteById(findId);
        userService.getById(findId2);
    }

    @Test
    public void testRequestPasswordReset() {
        String email = "test@example.com";

        PasswordResetToken passwordResetToken = new PasswordResetToken("test", user);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(jwtTokenProvider.generatePasswordResetToken(user.getId())).thenReturn("test");
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(passwordResetToken);
        when(mailService.sendPasswordResetMail(user.getEmail(), "test")).thenReturn(true);

        assertTrue(userService.requestPasswordReset(email));
    }

    @Test
    public void testResetPassword() {
        PasswordResetToken passwordResetToken = new PasswordResetToken("test", user);

        when(jwtTokenProvider.hasTokenExpired("test")).thenReturn(false);
        when(passwordResetTokenRepository.findByToken("test")).thenReturn(passwordResetToken);
        when(mailService.sendPasswordResetMail(user.getEmail(), "test")).thenReturn(true);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(passwordResetTokenRepository).delete(passwordResetToken);

        assertTrue(userService.resetPassword("test", user.getPassword()));

        when(jwtTokenProvider.hasTokenExpired("test")).thenReturn(true);
        assertFalse(userService.resetPassword("test", user.getPassword()));

        assertFalse(userService.resetPassword(null, user.getPassword()));

        when(jwtTokenProvider.hasTokenExpired("test")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(null);
        assertFalse(userService.resetPassword("test", user.getPassword()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_NotFound(){
        when(userRepository.findById(findId2))
                .thenThrow(new ResourceNotFoundException("User", "id", findId2));

        userService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound() throws IOException {
        UserWebRequest request = new UserWebRequest(user.getName(), user.getDescription(), user.getImage());

        when(userRepository.existsById(findId)).thenReturn(Boolean.FALSE);

        userService.save(request, findId);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        doThrow(new ResourceNotFoundException("User", "id", findId2)).when(userRepository).deleteById(findId2);

        userService.deleteById(findId2);
    }
}
