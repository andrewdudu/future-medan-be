package com.future.medan.backend.models.entity;

import com.future.medan.backend.models.constants.AdminConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
public class Admin extends BaseEntity {

    @Column(name = AdminConstant.ADMIN_USERNAME)
    private String username;

    @Column(name = AdminConstant.ADMIN_EMAIL)
    private String email;

    @Column(name = AdminConstant.ADMIN_PASSWORD)
    private String password;

}
