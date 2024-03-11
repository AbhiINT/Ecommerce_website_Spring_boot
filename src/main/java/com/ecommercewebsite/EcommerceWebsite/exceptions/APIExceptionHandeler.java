package com.ecommercewebsite.EcommerceWebsite.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommercewebsite.EcommerceWebsite.exceptions.exceptions.UserNotFoundException;

@ControllerAdvice
public class APIExceptionHandeler {
    @ExceptionHandler(value = { UserNotFoundException.class })
    public ResponseEntity<ApiException> handleApiRequestException(UserNotFoundException ex) {
        ApiException apiException = new ApiException();

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}