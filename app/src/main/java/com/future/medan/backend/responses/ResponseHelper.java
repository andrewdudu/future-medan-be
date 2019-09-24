package com.future.medan.backend.responses;

import org.springframework.http.HttpStatus;

public class ResponseHelper {

    /**
     * Create OK Response based on given data
     *
     * @param data
     * @param <T>
     * @return response ok
     */
    public static <T> Response<T> ok(T data) {
        Response<T> response = new Response<T>();

        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK.name());
        response.setData(data);

        return response;
    }
}
