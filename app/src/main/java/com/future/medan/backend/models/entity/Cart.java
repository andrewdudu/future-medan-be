package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.CartConstant;
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
@Table(name = "carts")
public class Cart extends BaseEntity {

    @Column(name = CartConstant.CART_QTY)
    private Integer qty;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "cart_product",
        joinColumns = @JoinColumn(name = CartConstant.CART_ID),
        inverseJoinColumns = @JoinColumn(name = CartConstant.CART_PRODUCT_ID)
    )
    private Set<Product> products;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CartConstant.CART_USER_ID)
    private User user;
}
