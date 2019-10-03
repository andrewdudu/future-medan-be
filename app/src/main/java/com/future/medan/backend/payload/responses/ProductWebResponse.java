package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWebResponse {

    private String name;

    private String sku;

    private String description;

    private Float price;

    private String image;

    private String author;
}
