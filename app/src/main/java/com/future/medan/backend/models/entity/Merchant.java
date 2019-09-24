package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.MerchantConstant;
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
@Table(name = "merchants")
public class Merchant extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = MerchantConstant.MERCHANT_USERNAME)
    private String username;

    @Column(name = MerchantConstant.MERCHANT_EMAIL)
    private String email;

    @Column(name = MerchantConstant.MERCHANT_PASSWORD)
    private String password;

    @Column(name = MerchantConstant.MERCHANT_DESCRIPTION)
    private String description;

    @Column(name = MerchantConstant.MERCHANT_IMAGE)
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Product> products;
}
