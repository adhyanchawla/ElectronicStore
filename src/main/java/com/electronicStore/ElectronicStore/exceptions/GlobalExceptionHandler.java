package com.electronicStore.ElectronicStore.exceptions;

import com.electronicStore.ElectronicStore.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // handle resource not found exception
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        logger.info("Exception Handler invoked!");
        ApiResponseMessage message = ApiResponseMessage.builder().message(ex.getMessage()).success(true).httpStatus(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodNotValidatedExceptionHandler(MethodArgumentNotValidException ex) {
        Map<String, Object> map = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        errors.stream().forEach((error)->{
            String message = error.getDefaultMessage();
            String field = ((FieldError)error).getField();
            map.put(field, message);
        });

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequest ex) {
        logger.info("Exception Handler invoked!");
        ApiResponseMessage message = ApiResponseMessage.builder().message(ex.getMessage()).success(false).httpStatus(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
