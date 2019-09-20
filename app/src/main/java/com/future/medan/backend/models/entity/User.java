package com.future.medan.backend.models.entity;

import com.future.medan.backend.models.constants.UserConstant;
import com.future.medan.backend.models.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = UserConstant.TABLE_NAME)
public class User extends BaseEntity {

    @NotNull(message = "{name.notnull}")
    @Column(name = UserConstant.USER_NAME)
    private String name;

    @Column(name = UserConstant.USER_DESCRIPTION)
    private String description;

    @Column(name = UserConstant.USER_EMAIL)
    private String email;

    @Column(name = UserConstant.USER_USERNAME)
    private String username;

    @Column(name = UserConstant.USER_IMAGE)
    private String image;

    @Column(name = UserConstant.USER_PASSWORD)
    private String password;

    @Column(name = UserConstant.USER_ROLE)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}
