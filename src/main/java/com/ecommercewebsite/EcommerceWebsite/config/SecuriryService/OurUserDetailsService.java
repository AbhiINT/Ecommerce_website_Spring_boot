package com.ecommercewebsite.EcommerceWebsite.config.SecuriryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommercewebsite.EcommerceWebsite.model.admin.repository.AdminRepository;
import com.ecommercewebsite.EcommerceWebsite.model.user.repository.UserRepository;

@Service
public class OurUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository ourUserRepo;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return ourUserRepo.findByEmail(username) == null ? adminRepository.findByEmail(username) : null;

    }

}