package com.future.medan.backend.payload.responses;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseWebResponse {

    private String order_id;

    private UserWebResponse merchant;

    private UserWebResponse user;

    private ProductWebResponse product;

    private String status;
}
