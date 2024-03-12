package com.ecommercewebsite.EcommerceWebsite.model.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommercewebsite.EcommerceWebsite.model.admin.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    Admin findByEmail(String username);

    boolean existsByEmail(String email);
    
}
