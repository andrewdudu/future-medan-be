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
    @OneToMany(mappedBy = "carts", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = CartConstant.PRODUCT_ID)
    private Set<Product> products;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CartConstant.USER_ID)
    private User user;

}
