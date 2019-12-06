package com.future.medan.backend.models.entity;

import com.future.medan.backend.constants.BaseEntityConstant;
import com.future.medan.backend.constants.CartConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CartConstant.USER_ID, unique = true)
    private User user;
}
