package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ProductWebRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0", inclusive = true)
    private BigDecimal price;

    @NotBlank
    private String author;

    @NotBlank
    private String category;

    @NotBlank
    private String ISBN;

    @NotBlank
    private String pdf;

    @NotBlank
    private String image;
}
