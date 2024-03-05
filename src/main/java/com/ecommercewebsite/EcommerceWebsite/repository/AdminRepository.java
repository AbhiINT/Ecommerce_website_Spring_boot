package com.ecommercewebsite.EcommerceWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ecommercewebsite.EcommerceWebsite.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    Admin findByEmail(String username);
    
}
