package com.future.medan.backend.models.entity;

import com.future.medan.backend.constants.ProductConstant;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = ProductConstant.PRODUCT_SKU)
    private String sku;

    @Column(name = ProductConstant.PRODUCT_VARIANT)
    private String variant;

    @Column(name = ProductConstant.PRODUCT_DESCRIPTION)
    private String description;

    @Column(name = ProductConstant.PRODUCT_PRICE)
    private BigDecimal price;

    @Column(name = ProductConstant.PRODUCT_IMAGE)
    private String image;

    @Column(name = ProductConstant.PRODUCT_PDF)
    private String pdf;

    @Column(name = ProductConstant.PRODUCT_AUTHOR)
    private String author;

    @Column(name = ProductConstant.PRODUCT_HIDDEN)
    private Boolean hidden;

    @PrePersist
    public void prePersist() {
        this.hidden = false;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ProductConstant.MERCHANT_ID)
    private User merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ProductConstant.CATEGORY_ID)
    private Category category;

    public boolean getHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
