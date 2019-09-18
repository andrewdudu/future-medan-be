package com.future.medan.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.model.constants.CategoryConstant;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = CategoryConstant.TABLE_NAME)
public class Category extends BaseEntity {

    @Column(name = CategoryConstant.CATEGORY_NAME)
    private String name;

    @Column(name = CategoryConstant.CATEGORY_DESCRIPTION)
    private String description;

    @Column(name = CategoryConstant.CATEGORY_IMAGE)
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Product> products;

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
