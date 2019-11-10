package com.future.medan.backend.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileWebResponse {

    private String name;
    private String uri;
    private String type;
    private long size;
}
