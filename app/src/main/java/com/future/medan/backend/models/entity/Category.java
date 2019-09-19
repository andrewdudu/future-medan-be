package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
}
