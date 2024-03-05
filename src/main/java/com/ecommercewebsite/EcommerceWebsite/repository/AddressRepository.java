package com.ecommercewebsite.EcommerceWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommercewebsite.EcommerceWebsite.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {
    
}
