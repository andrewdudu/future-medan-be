package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewWebResponse {

    private Integer rating;

    private String comment;

    private ProductWebResponse product;

    private UserWebResponse user;
}
