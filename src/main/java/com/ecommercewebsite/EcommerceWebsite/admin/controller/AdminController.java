package com.ecommercewebsite.EcommerceWebsite.admin.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    

}
