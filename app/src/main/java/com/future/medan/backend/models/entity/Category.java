package com.future.medan.backend.models.entity;

import com.future.medan.backend.models.constants.CategoryConstant;
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
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = CategoryConstant.CATEGORY_DESCRIPTION)
    private String description;

    @Column(name = CategoryConstant.CATEGORY_IMAGE)
    private String image;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = CategoryConstant.PRODUCT_ID)
    private Set<Product> products;
}
