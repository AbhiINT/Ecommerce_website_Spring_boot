package com.ecommercewebsite.EcommerceWebsite.model.user.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommercewebsite.EcommerceWebsite.model.user.entity.User;

public interface UserRepository extends JpaRepository<User , Long> {

   public User findByEmail(String email);

   public boolean existsByEmail(String email);
    
}
