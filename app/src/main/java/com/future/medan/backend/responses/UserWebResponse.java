package com.future.medan.backend.responses;

import com.future.medan.backend.models.constants.UserConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWebResponse {

    private String name;

    private String description;

    private String email;

    private String username;

    private String image;

    private String password;
}
