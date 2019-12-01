package com.future.medan.backend.payload.responses;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseHelper {

    /**
     * Create OK Response based on given data
     *
     * @param data
     * @param <T>
     * @return response ok
     */
    public static <T> Response<T> ok(T data) {
        Response<T> response = new Response<>();

        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK.name());
        response.setData(data);

        return response;
    }

    /**
     * Create OK Response based on given data
     *
     * @param data
     * @param <T>
     * @return response ok
     */
    public static <T> PaginationResponse<T> ok(T data, long totalElements, int totalPages) {
        PaginationResponse<T> response = new PaginationResponse<>();

        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK.name());
        response.setData(data);
        response.setTotalElements(totalElements);
        response.setTotalPages(totalPages);

        return response;
    }

    public static <T> ErrorResponse<T> error(HttpStatus status, T message) {
        ErrorResponse<T> response = new ErrorResponse<>();

        response.setTimestamp(LocalDateTime.now());
        response.setCode(status.value());
        response.setStatus(status.name());
        response.setMessage(message);

        return response;
    }
}
