package com.future.medan.backend.payload.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PdfFileWebRequest {

    @NotBlank
    private String filePath;

    @NotBlank
    private String productId;
}
