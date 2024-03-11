package com.ecommercewebsite.EcommerceWebsite.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommercewebsite.EcommerceWebsite.admin.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    Admin findByEmail(String username);
    
}
