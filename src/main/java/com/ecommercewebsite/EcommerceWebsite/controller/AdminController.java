package com.ecommercewebsite.EcommerceWebsite.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommercewebsite.EcommerceWebsite.DTO.ChangePasswordDTO;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.entity.User;
import com.ecommercewebsite.EcommerceWebsite.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

   private final UserRepository ourUserRepo;
   private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/add_all_products")
    public String addAllProducts() {
        //return adminService.loadWebsite();
        return null;
    }
    public ReqRes changePassword(ChangePasswordDTO changePasswordDTO) {
        ReqRes response = new ReqRes();
        try {
            // Retrieve the user by email
            User user = ourUserRepo.findByEmail(changePasswordDTO.getEmail());
    
            // Check if the provided old password matches the stored password
            if (passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
                // Update the password with the new one
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                ourUserRepo.save(user);
    
                response.setStatusCode(200);
                response.setMessage("Password changed successfully.");
            } else {
                response.setStatusCode(400); // Bad Request
                response.setError("Old password does not match.");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Internal Server Error. Please try again.");
    
            e.printStackTrace();
        }
        return response;
    }

}
