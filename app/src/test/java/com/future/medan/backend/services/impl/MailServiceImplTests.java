package com.future.medan.backend.services.impl;

import com.future.medan.backend.services.MailService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

public class MailServiceImplTests {

    @Mock
    private JavaMailSender javaMailSender;

    @Value("${local.server.port}")
    private int port;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    private MailService mailService;

    private String email, url;

    private SimpleMailMessage msg;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mailService = new MailServiceImpl(javaMailSender);

        this.email = "example@test.com";
        this.url = "localhost:" + port + "/future-medan/api/password-reset";

        this.msg = new SimpleMailMessage();
    }

    @Test
    public void testSendPasswordResetMail_OK() {

        msg.setTo(email);
        msg.setSubject("Reset Password");
        msg.setText(url);

        doNothing().when(javaMailSender).send(msg);

        assertTrue(mailService.sendPasswordResetMail(email, url));
    }
}
