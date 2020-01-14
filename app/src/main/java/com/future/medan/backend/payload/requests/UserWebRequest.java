package com.future.medan.backend.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWebRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;
}
