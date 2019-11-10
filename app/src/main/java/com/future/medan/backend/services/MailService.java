package com.future.medan.backend.services;

public interface MailService {

    boolean sendPasswordResetMail(String email, String token);
}
