package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FileWebResponse {

    private String base64;
}
