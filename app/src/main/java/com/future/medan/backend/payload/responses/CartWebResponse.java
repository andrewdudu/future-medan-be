package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class CartWebResponse {

    private Set<ProductWebResponse> products;

    private UserWebResponse user;
}
