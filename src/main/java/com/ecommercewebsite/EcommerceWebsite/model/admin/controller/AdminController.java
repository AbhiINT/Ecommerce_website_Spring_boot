package com.ecommercewebsite.EcommerceWebsite.model.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommercewebsite.EcommerceWebsite.DTO.ChangePasswordDTO;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.model.admin.service.AdminService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  
   private final AdminService adminService;

    @GetMapping("/add_all_products")
    public String addAllProducts() {
        //return adminService.loadWebsite();
        return null;
    }
    @PostMapping("/chnage-password")
    public ResponseEntity<ReqRes> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        ReqRes response = adminService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ReqRes> forgotPassword(@RequestBody String email)
    {
        return ResponseEntity.ok(adminService.forgotPassword(email));
    }

    
    

}
