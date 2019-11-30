package com.future.medan.backend.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaginationResponse<T> {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private T data;

    @JsonProperty("total_elements")
    private long totalElements;

    @JsonProperty("total_pages")
    private int totalPages;
}
