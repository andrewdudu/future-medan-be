package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.WishlistConstant;
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
@Table(name = "wishlists")
public class Wishlist extends BaseEntity {

    @Column(name = WishlistConstant.WISHLIST_QTY)
    private Integer qty;

    @JsonIgnore
    @ManyToMany(mappedBy = "wishlists", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JoinColumn(name = WishlistConstant.PRODUCT_ID)
    private Set<Product> product;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = WishlistConstant.USER_ID)
    private User user;
}
