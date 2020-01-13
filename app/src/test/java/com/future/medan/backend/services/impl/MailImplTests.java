package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.services.MailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import sun.rmi.runtime.Log;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class MailImplTests {

    @Mock
    private JavaMailSender javaMailSender;

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    private MailService mailService;

    private User user;

    private String email, token, url, userId;

    private SimpleMailMessage msg;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mailService = new MailServiceImpl(javaMailSender);

        this.email = "example@test.com";
        this.url = "localhost:" + port + "/future-medan/api/password-reset";
        this.userId = "user-test-id";

        this.msg = new SimpleMailMessage();

        this.user = User.builder()
                .name("User Test")
                .username("user-test")
                .email("test@example.com")
                .description("")
                .password("Test")
                .status(true)
                .image("/api/get-img/1cab35be-cb0f-4db9-87f9-7b47db38f7ac.png")
                .roles(new HashSet<>(Collections.singleton(new Role(RoleEnum.ROLE_USER))))
                .build();

        this.token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Test
    public void testSendPasswordResetMail_OK() {

        msg.setTo(email);

        msg.setSubject("Reset Password");
        msg.setText(url + "?token=" + token);

        javaMailSender.send(msg);

    }
}
