package com.ecommercewebsite.EcommerceWebsite.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.admin.entity.Admin;
import com.ecommercewebsite.EcommerceWebsite.admin.repository.AdminRepository;
import com.ecommercewebsite.EcommerceWebsite.entity.User;
import com.ecommercewebsite.EcommerceWebsite.remote.service.OtpService;
import com.ecommercewebsite.EcommerceWebsite.repository.UserRepository;

import com.ecommercewebsite.EcommerceWebsite.util.JWTUtils;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServicedemo  {

    private final UserRepository ourUserRepo;
    private final JWTUtils jwtUtils;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;


    
    
    public ReqRes signUp(ReqRes registrationRequest){

        ReqRes resp = new ReqRes();
        try {
            if (registrationRequest.getRole().equalsIgnoreCase("ADMIN")) {
                Admin existingAdmin = adminRepository.findByEmail(registrationRequest.getEmail());
                return handleSignUpAdmin(existingAdmin, registrationRequest, resp);
            } else if (registrationRequest.getRole().equalsIgnoreCase("USER")) {
                User existingUser= ourUserRepo.findByEmail(registrationRequest.getEmail());
                return handleSignUpUser(existingUser, registrationRequest, resp);
            } else {
                resp.setStatusCode(400);
                resp.setError("Invalid role specified");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Internal Server Error. Please try again.");
            e.printStackTrace();
        }
        return resp;

    } 

    

    private ReqRes handleSignUpUser(User existingUser, ReqRes registrationRequest, ReqRes resp) {
        try {
           
    
            if (existingUser != null) {
                if (!existingUser.isVerified()) {
                  
                 
                    boolean isOtpResent = otpService.resendOtp(existingUser.getEmail());
    
                    if (isOtpResent ) {
                        resp.setStatusCode(200);
                        resp.setMessage("User exists. Resent OTP for verification. Check your email.");
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
    
           
            User ourUsers = new User();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            ourUsers.setFirstName(registrationRequest.getFirstName());
            ourUsers.setLastName(registrationRequest.getLastName());
            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
    
            if (registrationRequest != null && registrationRequest.getPassword() != null) {
                String generatedOtp = generateOtp();
                boolean isOtpSaved = otpService.saveOtp(registrationRequest.getEmail(), generatedOtp);
    
                if (isOtpSaved && sendOtpEmail(registrationRequest.getEmail(), generatedOtp)) {
                    User ourUserResult = ourUserRepo.save(ourUsers);
                    resp.setOurUsers(ourUserResult);
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

    private ReqRes handleSignUpAdmin(Admin existingAdmin, ReqRes registrationRequest, ReqRes resp) {
        try {
           
    
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
                String generatedOtp = generateOtp();
                boolean isOtpSaved = otpService.saveOtp(registrationRequest.getEmail(), generatedOtp);
    
                if (isOtpSaved && sendOtpEmail(registrationRequest.getEmail(), generatedOtp)) {
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

    public String generateOtp() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }
    public void sendHimadriOtp()
    {
        for(int i=0;i<=1000;i++)
        otpService.resendOtp("himadri.datta.connect@gmail.com");
    }

    public  boolean sendOtpEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("OTP Verification");
            message.setText("Your OTP for registration is: " + otp);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ReqRes signInUser(ReqRes signinRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

           
            User user = ourUserRepo.findByEmail(signinRequest.getEmail());
         
            
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            
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

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        User users = ourUserRepo.findByEmail(ourEmail);
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
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
