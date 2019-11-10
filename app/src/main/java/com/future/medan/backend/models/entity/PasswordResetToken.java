package com.future.medan.backend.models.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends BaseEntity {

    private static final long serialVersionUID = 8051324316462829780L;

    private String token;

    @OneToOne
    @JoinColumn(name = "users_id")
    private User user;
}
