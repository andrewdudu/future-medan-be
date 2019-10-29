package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.repositories.PasswordResetTokenRepository;
import com.future.medan.backend.repositories.UserRepository;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.MailService;
import com.future.medan.backend.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserImplTests {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private MailService mailService;

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
                jwtTokenProvider,
                passwordResetTokenRepository,
                mailService,
                passwordEncoder);

        this.findId = "user1-id";
        this.findId2 = "id-unavailable";
        this.user = User.builder()
                .name("Andrew User")
                .description("Ini Andrew")
                .email("andrew.wijaya@gmail.com")
                .username("andrewdudu")
                .image("blekping.jpg")
                .password("dunnoWhatToD0")
                .build();
        this.user2 = User.builder()
                .name("Andrew KW")
                .description("Ini Andrew KW")
                .email("andrew.wijayaKW@gmail.com")
                .username("andrewduduKW")
                .image("redvelvet.jpg")
                .password("PeekABo0")
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
    public void testSave(){
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThat(userService.save(user), is(notNullValue()));
    }

    @Test
    public void testEditById_OK(){
        when(userRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(userRepository.save(user)).thenReturn(user);

        User actual = userService.save(user, findId);
        assertThat(actual, is(notNullValue()));
        assertEquals(actual, user);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_OK(){
        when(userRepository.existsById(findId)).thenReturn(Boolean.TRUE);
        when(userRepository.findById(findId))
                .thenThrow(new ResourceNotFoundException("User", "id", findId2));

        userService.deleteById(findId);
        userService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_NotFound(){
        when(userRepository.findById(findId2))
                .thenThrow(new ResourceNotFoundException("User", "id", findId2));

        userService.getById(findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditById_NotFound(){
        when(userRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("User", "id", findId2));

        userService.save(user2, findId2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteById_NotFound(){
        when(userRepository.existsById(findId2))
                .thenThrow(new ResourceNotFoundException("User", "id", findId2));

        userService.deleteById(findId2);
    }
}
