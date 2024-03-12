package com.ecommercewebsite.EcommerceWebsite.model.admin.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ecommercewebsite.EcommerceWebsite.DTO.ChangePasswordDTO;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.model.admin.entity.Admin;
import com.ecommercewebsite.EcommerceWebsite.model.admin.repository.AdminRepository;
import com.ecommercewebsite.EcommerceWebsite.model.user.entity.User;
import com.ecommercewebsite.EcommerceWebsite.model.user.repository.UserRepository;
import com.ecommercewebsite.EcommerceWebsite.remote.otp.service.OtpService;
import com.ecommercewebsite.EcommerceWebsite.repository.OtpRepository;
import com.ecommercewebsite.EcommerceWebsite.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AdminService {

    private final String apiUrl = "https://dummyjson.com/products";
    private final RestTemplate restTemplate;
  
    private final  ProductsRepository productsRepository;
  
    private final BCryptPasswordEncoder passwordEncoder;
  
    private final AdminRepository adminRepository;
    private final OtpRepository otpRepository;
    private final OtpService otpService;

    

    // public String loadWebsite() {
    //     try {
    //         ResponseEntity<ProductsResponse> responseEntity = restTemplate.exchange(
    //             apiUrl,
    //             HttpMethod.GET,
    //             null,
    //             ProductsResponse.class
    //         );

    //         ProductsResponse productsResponse = responseEntity.getBody();

    //         if (productsResponse != null && productsResponse.getProducts() != null) {
    //             productsRepository.saveAll(productsResponse.getProducts());
    //             return "Products loaded and stored successfully!";
    //         } else {
    //             return "No products found in the response.";
    //         }

    //     } catch (HttpClientErrorException ex) {
    //         if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
    //             return "API endpoint not found.";
    //         } else {
    //             return "Failed to fetch and store products.";
    //         }
    //     }
    // }

    public ReqRes changePassword(ChangePasswordDTO changePasswordDTO) {
    ReqRes response = new ReqRes();
    try {
        
        Admin user = adminRepository.findByEmail(changePasswordDTO.getEmail());

       
        if (passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
           
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            adminRepository.save(user);

            response.setStatusCode(200);
            response.setMessage("Password changed successfully.");
        } else {
            response.setStatusCode(400); 
            response.setError("Old password does not match.");
        }
    } catch (Exception e) {
        response.setStatusCode(500);
        response.setError("Internal Server Error. Please try again.");

        e.printStackTrace();
    }
    return response;
}



    public ReqRes forgotPassword(String email) {
        otpRepository.deleteAll();
        ReqRes reqRes=new ReqRes();
        if(adminRepository.existsByEmail(email))
        {
            
            otpService.sendOtpEmail(email, otpService.generateOtp());
            reqRes.setStatusCode(200);
            reqRes.setMessage("Otp Sent to "+email +" for Password Chnage");
        }
        return reqRes;


        
        
    }

}