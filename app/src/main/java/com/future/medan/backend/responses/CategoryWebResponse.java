package com.future.medan.backend.responses;

import com.future.medan.backend.models.constants.CategoryConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryWebResponse {

    private Integer parent_id;

    private String name;

    private String description;

    private String image;
}
