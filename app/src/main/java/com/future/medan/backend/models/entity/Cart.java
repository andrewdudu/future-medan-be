package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.CartConstant;
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
@Table(name = "carts")
public class Cart extends BaseEntity {

    @Column(name = CartConstant.CART_QTY)
    private Integer qty;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CartConstant.PRODUCT_ID)
//    @JoinTable(
//        name = "cart_product",
//        joinColumns = @JoinColumn(name = CartConstant.CART_ID),
//        inverseJoinColumns = @JoinColumn(name = CartConstant.PRODUCT_ID)
//    )
    private Product product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CartConstant.USER_ID)
    private User user;

    @JsonIgnore
    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Purchase purchase;
}
