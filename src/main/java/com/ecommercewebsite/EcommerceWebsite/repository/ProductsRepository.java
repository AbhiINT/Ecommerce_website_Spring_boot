package com.ecommercewebsite.EcommerceWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommercewebsite.EcommerceWebsite.entity.Product;

public interface ProductsRepository extends JpaRepository<Product, Long> {

}
