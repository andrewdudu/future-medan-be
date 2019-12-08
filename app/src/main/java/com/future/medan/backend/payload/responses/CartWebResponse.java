package com.future.medan.backend.payload.responses;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class CartWebResponse {

    private Set<ProductWebResponse> products;

    private UserWebResponse user;
}
