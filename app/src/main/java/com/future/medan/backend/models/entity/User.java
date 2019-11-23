package com.future.medan.backend.models.entity;

import com.future.medan.backend.constants.UserConstant;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

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

    @Column(name = UserConstant.USER_STATUS)
    private Boolean status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
