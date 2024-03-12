package com.ecommercewebsite.EcommerceWebsite.DTO;

import java.util.List;

import com.ecommercewebsite.EcommerceWebsite.entity.Product;
import com.ecommercewebsite.EcommerceWebsite.model.admin.entity.Admin;
import com.ecommercewebsite.EcommerceWebsite.model.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private List<Product> data;

    private User ourUsers;
    private Admin ourAdmin;

}