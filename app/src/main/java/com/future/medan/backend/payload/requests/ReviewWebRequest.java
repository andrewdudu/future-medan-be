package com.future.medan.backend.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWebRequest {

    @DecimalMin(value = "1")
    @DecimalMax(value = "5")
    private Integer rating;

    @NotBlank
    private String comment;

    @NotBlank
    private String productId;
}
