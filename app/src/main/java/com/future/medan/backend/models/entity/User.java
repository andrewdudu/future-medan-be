package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.UserConstant;
import com.future.medan.backend.models.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "name")
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

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Wishlist> wishlists;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Purchase> purchases;
}
