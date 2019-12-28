package com.future.medan.backend.exceptions;

import com.future.medan.backend.payload.responses.ErrorResponse;
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
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleException(ResourceNotFoundException ex){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleException(AppException ex){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    private void log (Exception ex){
        Logger logger = LoggerFactory.getLogger(ex.getClass());
        logger.error(ex.getMessage());
    }
}
