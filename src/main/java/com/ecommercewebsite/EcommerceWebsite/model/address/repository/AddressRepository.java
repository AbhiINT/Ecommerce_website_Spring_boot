package com.ecommercewebsite.EcommerceWebsite.model.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommercewebsite.EcommerceWebsite.model.address.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {
    
}
