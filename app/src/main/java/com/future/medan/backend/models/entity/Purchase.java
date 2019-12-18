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

    @Column(name = PurchaseConstant.ORDER_ID)
    private String orderId;

    @Column(name = PurchaseConstant.STATUS)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PurchaseConstant.USER_ID)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PurchaseConstant.MERCHANT_ID)
    private User merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PurchaseConstant.PRODUCT_ID)
    private Product product;
}
