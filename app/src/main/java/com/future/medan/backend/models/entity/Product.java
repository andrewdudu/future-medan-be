package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.ProductConstant;
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
@Table(name = ProductConstant.TABLE_NAME)
public class Product extends BaseEntity {

    @Column(name = ProductConstant.PRODUCT_NAME)
    private String name;

    @Column(name = ProductConstant.PRODUCT_SKU)
    private String sku;

    @Column(name = ProductConstant.PRODUCT_DESCRIPTION)
    private String description;

    @Column(name = ProductConstant.PRODUCT_PRICE)
    private Float price;

    @Column(name = ProductConstant.PRODUCT_IMAGE)
    private String image;

    @Column(name = ProductConstant.PRODUCT_AUTHOR)
    private String author;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ProductConstant.CATEGORY_FK)
    private Category category;
}
