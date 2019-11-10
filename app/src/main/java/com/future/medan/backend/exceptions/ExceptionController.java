package com.future.medan.backend.exceptions;

import com.future.medan.backend.payload.responses.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleException(BadRequestException ex){
        return errorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleException(ResourceNotFoundException ex){
        return errorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleException(AppException ex){
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<Object> errorResponse (HttpStatus httpStatus, Exception ex){
        Logger logger = LoggerFactory.getLogger(ex.getClass());
        logger.error(ex.getMessage());

        return ResponseEntity.status(httpStatus).body(logger);
        // ResponseHelper.error(httpStatus, ex.getMessage())
    }
}
