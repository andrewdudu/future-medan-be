package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.ProductConstant;
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
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "name")
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
    @JoinColumn(name = ProductConstant.CATEGORY_ID)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Wishlist> wishlists;
}
