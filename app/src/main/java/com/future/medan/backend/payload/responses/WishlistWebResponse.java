package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class WishlistWebResponse {

    private UserWebResponse user;

    private Set<ProductWebResponse> products;
}
