package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.PurchaseConstant;
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
@Table(name = "purchases")
public class Purchase extends BaseEntity {

    @Column(name = PurchaseConstant.PURCHASE_PRICE)
    private Float price;

    @Column(name = PurchaseConstant.PURCHASE_AUTHOR_NAME)
    private String author_name;

    @Column(name = PurchaseConstant.PURCHASE_PRODUCT_DESCRIPTION)
    private String product_description;

    @Column(name = PurchaseConstant.PURCHASE_PRODUCT_IMAGE)
    private String product_image;

    @Column(name = PurchaseConstant.PURCHASE_PRODUCT_NAME)
    private String product_name;

    @Column(name = PurchaseConstant.PURCHASE_PRODUCT_SKU)
    private String product_sku;

    @Column(name = PurchaseConstant.PURCHASE_QTY)
    private Integer qty;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PurchaseConstant.USER_ID)
    private User user;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PurchaseConstant.CART_ID)
    private Cart cart;
}
