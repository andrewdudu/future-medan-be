package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryWebRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String image;

    @NotBlank
    private Boolean hidden;
}
