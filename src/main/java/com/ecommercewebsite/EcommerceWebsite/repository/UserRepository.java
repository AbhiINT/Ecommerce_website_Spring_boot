package com.ecommercewebsite.EcommerceWebsite.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommercewebsite.EcommerceWebsite.entity.User;

public interface UserRepository extends JpaRepository<User , Long> {

   public User findByEmail(String email);

   public boolean existsByEmail(String email);
    
}
