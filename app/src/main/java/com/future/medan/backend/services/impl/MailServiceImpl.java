package com.future.medan.backend.services.impl;

import com.future.medan.backend.services.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private JavaMailSender javaMailSender;

    @Value("${frontend.url}/password-reset")
    private String url;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendPasswordResetMail(String email, String token) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Reset Password");
        msg.setText(url + "?token=" + token);

        javaMailSender.send(msg);

        return true;
    }
}
