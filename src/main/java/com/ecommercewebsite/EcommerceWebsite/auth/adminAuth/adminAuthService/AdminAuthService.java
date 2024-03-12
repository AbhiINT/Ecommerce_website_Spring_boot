package com.ecommercewebsite.EcommerceWebsite.auth.adminAuth.adminAuthService;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommercewebsite.EcommerceWebsite.DTO.ChangePasswordDTO;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.auth.dto.LoginRequest;
import com.ecommercewebsite.EcommerceWebsite.auth.dto.RegistrationRequest;
import com.ecommercewebsite.EcommerceWebsite.auth.dto.RegistrationResponse;
import com.ecommercewebsite.EcommerceWebsite.model.admin.entity.Admin;
import com.ecommercewebsite.EcommerceWebsite.model.admin.repository.AdminRepository;
import com.ecommercewebsite.EcommerceWebsite.remote.otp.service.OtpService;
import com.ecommercewebsite.EcommerceWebsite.util.JWTUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAuthService {
    private final OtpService otpService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
     public RegistrationResponse handleSignUpAdmin(RegistrationRequest registrationRequest) {
        RegistrationResponse resp=new RegistrationResponse();
        try {
           
         Admin existingAdmin =adminRepository.findByEmail(registrationRequest.getEmail());
        
            if (existingAdmin != null) {
                if (!existingAdmin.isVerified()) {
               
                  
                    boolean isOtpResent = otpService.resendOtp(existingAdmin.getEmail());
    
                    if (isOtpResent) {
                        resp.setHttp(HttpStatus.CONFLICT);
                        resp.setEmail(existingAdmin.getEmail());
                        resp.setMessage("Admin exists. Resent OTP for verification. Check your email.");
                    } else {
                        resp.setHttp(HttpStatus.INTERNAL_SERVER_ERROR);
                        resp.setMessage("Failed to resend OTP. Please try again.");
                    }
                    return resp;
                } else {
             
                    resp.setHttp(HttpStatus.FOUND); 
                    resp.setMessage("User with this email already exists.");
                    return resp;
                }
            }
    
        
            Admin ourUsers = new Admin();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setFirstName(registrationRequest.getFirstName());
            ourUsers.setLastName(registrationRequest.getLastName());
            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
    
            if (registrationRequest != null && registrationRequest.getPassword() != null) {
                String generatedOtp = otpService.generateOtp();
                boolean isOtpSaved = otpService.saveOtp(registrationRequest.getEmail(), generatedOtp);
    
                if (isOtpSaved && otpService.sendOtpEmail(registrationRequest.getEmail(), generatedOtp)) {
                    adminRepository.save(ourUsers);
                    resp.setMessage("User Registered Successfully. Check your email for OTP verification.");
                    resp.setEmail(registrationRequest.getEmail());
                    resp.setHttp(HttpStatus.OK);
                } else {
                    resp.setHttp(HttpStatus.INTERNAL_SERVER_ERROR);
                    resp.setMessage("Failed to save or send OTP. Please try again.");
                }
            }
        } catch (Exception e) {
            resp.setHttp(HttpStatus.INTERNAL_SERVER_ERROR);
            resp.setMessage("Internal Server Error. Please try again.");
    
            e.printStackTrace();
        }
        return resp;
    }

     public ReqRes signInAdmin(LoginRequest signinRequest)
    {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

           
         
            Admin admin=adminRepository.findByEmail(signinRequest.getEmail());
            
            String jwt = jwtUtils.generateToken(admin);
            String refreshToken = jwtUtils.generateRefreshToken( admin);

            
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("3 MINUTES");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {
            
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    
     public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        Admin admin = adminRepository.findByEmail(ourEmail);
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), admin)) {
            var jwt = jwtUtils.generateToken(admin);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }
}
