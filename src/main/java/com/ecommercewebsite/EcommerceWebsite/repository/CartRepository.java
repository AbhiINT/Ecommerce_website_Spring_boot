package com.ecommercewebsite.EcommerceWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommercewebsite.EcommerceWebsite.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
