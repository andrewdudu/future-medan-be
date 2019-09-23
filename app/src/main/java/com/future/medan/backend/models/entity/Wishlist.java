package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.ReviewConstant;
import com.future.medan.backend.models.constants.WishlistConstant;
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
@Table(name = "wishlists")
public class Wishlist extends BaseEntity {

    @Column(name = WishlistConstant.WISHLIST_QTY)
    private Integer qty;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = WishlistConstant.PRODUCT_ID)
    private Product product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = WishlistConstant.USER_ID)
    private User user;
}
