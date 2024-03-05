package com.ecommercewebsite.EcommerceWebsite.service;

import org.springframework.stereotype.Service;

import com.ecommercewebsite.EcommerceWebsite.entity.Address;
import com.ecommercewebsite.EcommerceWebsite.entity.User;
import com.ecommercewebsite.EcommerceWebsite.repository.AddressRepository;
import com.ecommercewebsite.EcommerceWebsite.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public String addAddress(Address address, Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);

            if (user != null) {
                Address newAddress = new Address();
                newAddress.setLocality(address.getLocality());
                newAddress.setCity(address.getCity());
                newAddress.setDistrict(address.getDistrict());
                newAddress.setState(address.getState());
                newAddress.setPincode(address.getPincode());

                newAddress.getUsers().add(user);
                addressRepository.save(newAddress);

                user.setAddress(newAddress); 
                userRepository.save(user);

                return "Address added successfully";
            } else {
                return "User not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding address";
        }
    }
}
