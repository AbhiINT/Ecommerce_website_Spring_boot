package com.ecommercewebsite.EcommerceWebsite.auth.dto;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationResponse {
    private HttpStatusCode http;
    private String email;
    private String message;
    
    
}
