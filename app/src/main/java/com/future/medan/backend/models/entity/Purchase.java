package com.future.medan.backend.models.entity;

import com.future.medan.backend.constants.PurchaseConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchase extends BaseEntity {

    @Column(name = PurchaseConstant.PURCHASE_PRICE)
    private BigDecimal price;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PurchaseConstant.USER_ID)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PurchaseConstant.PRODUCT_ID)
    private Product product;
}
