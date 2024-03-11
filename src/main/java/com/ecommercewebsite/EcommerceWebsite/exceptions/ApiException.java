package com.ecommercewebsite.EcommerceWebsite.exceptions;



import org.springframework.http.HttpStatus;

public class ApiException {
    private HttpStatus httpStatus;
     private String message;
     private String ErrorDetails;
     private String support;

    public ApiException(String message, String ErrorDetails, String support, HttpStatus httpStatus) {
        this.message = message;
        this.ErrorDetails = ErrorDetails;
        this.support = support;
        this.httpStatus = httpStatus;
      
    }
    public ApiException()
    {
        
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

  

    public String getErrorDetails() {
        return ErrorDetails;
    }

    public String getSupport() {
        return support;
    }
}
