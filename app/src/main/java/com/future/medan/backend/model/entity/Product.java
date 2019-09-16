package com.future.medan.backend.model.entity;

import com.future.medan.backend.constant.ProductConstant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = ProductConstant.TABLE_NAME)
public class Product extends BaseEntity {

    @Column(name = ProductConstant.PRODUCCT_NAME)
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
}
