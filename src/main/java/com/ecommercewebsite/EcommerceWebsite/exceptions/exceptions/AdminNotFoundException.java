package com.ecommercewebsite.EcommerceWebsite.exceptions.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminNotFoundException extends RuntimeException {
      private  HttpStatus httpStatus;
      private String errorMessage;
      private String ErrorDetails;
      private String support;

}
