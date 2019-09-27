package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.UserConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = UserConstant.USER_ROLE)
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Cart> carts;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Wishlist wishlist;
}
