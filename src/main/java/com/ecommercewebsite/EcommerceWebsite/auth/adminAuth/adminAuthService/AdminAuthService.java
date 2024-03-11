package com.ecommercewebsite.EcommerceWebsite.auth.adminAuth.adminAuthService;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommercewebsite.EcommerceWebsite.DTO.ChangePasswordDTO;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.admin.entity.Admin;
import com.ecommercewebsite.EcommerceWebsite.admin.repository.AdminRepository;

import com.ecommercewebsite.EcommerceWebsite.remote.service.OtpService;
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
     public ReqRes handleSignUpAdmin(ReqRes registrationRequest) {
        ReqRes resp=new ReqRes();
        try {
           
         Admin existingAdmin =adminRepository.findByEmail(registrationRequest.getEmail());
        
            if (existingAdmin != null) {
                if (!existingAdmin.isVerified()) {
               
                  
                    boolean isOtpResent = otpService.resendOtp(existingAdmin.getEmail());
    
                    if (isOtpResent) {
                        resp.setStatusCode(200);
                        resp.setMessage("Admin exists. Resent OTP for verification. Check your email.");
                    } else {
                        resp.setStatusCode(500);
                        resp.setError("Failed to resend OTP. Please try again.");
                    }
                    return resp;
                } else {
             
                    resp.setStatusCode(400); 
                    resp.setMessage("User with this email already exists.");
                    return resp;
                }
            }
    
        
            Admin ourUsers = new Admin();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            ourUsers.setFirstName(registrationRequest.getFirstName());
            ourUsers.setLastName(registrationRequest.getLastName());
            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
    
            if (registrationRequest != null && registrationRequest.getPassword() != null) {
                String generatedOtp = otpService.generateOtp();
                boolean isOtpSaved = otpService.saveOtp(registrationRequest.getEmail(), generatedOtp);
    
                if (isOtpSaved && otpService.sendOtpEmail(registrationRequest.getEmail(), generatedOtp)) {
                    Admin ourUserResult = adminRepository.save(ourUsers);
                    resp.setOurAdmin(ourUserResult);
                    resp.setMessage("User Registered Successfully. Check your email for OTP verification.");
                    resp.setStatusCode(200);
                } else {
                    resp.setStatusCode(500);
                    resp.setError("Failed to save or send OTP. Please try again.");
                }
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Internal Server Error. Please try again.");
    
            e.printStackTrace();
        }
        return resp;
    }

     public ReqRes signInAdmin(ReqRes signinRequest)
    {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

           
         
            Admin admin=adminRepository.findByEmail(signinRequest.getEmail());
            
            var jwt = jwtUtils.generateToken(admin);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), admin);

            
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {
            
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes changePassword(ChangePasswordDTO changePasswordDTO)
    {
        ReqRes resp=new ReqRes();
        Admin admin=adminRepository.findByEmail(changePasswordDTO.getEmail());

        if(admin.getPassword().matches(changePasswordDTO.getCurrentPassword()))
        {
            admin.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            adminRepository.save(admin);
            resp.setStatusCode(200);
            resp.setMessage("Password Chnaged Successfully");
            return resp;
        }
        resp.setStatusCode(400);
        resp.setMessage("Wrong Old Password");
        return resp;

        
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
